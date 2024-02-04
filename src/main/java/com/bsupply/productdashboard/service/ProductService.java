package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.PageResponseDto;
import com.bsupply.productdashboard.dto.request.ProductRequest;
import com.bsupply.productdashboard.dto.response.ProductCategoryResponse;
import com.bsupply.productdashboard.dto.response.ProductResponse;
import com.bsupply.productdashboard.entity.Product;
import com.bsupply.productdashboard.entity.ProductCategory;
import com.bsupply.productdashboard.enums.MeasurementUnits;
import com.bsupply.productdashboard.exception.DuplicateProductNameException;
import com.bsupply.productdashboard.exception.ProductCategoryNotFoundException;
import com.bsupply.productdashboard.exception.ProductNotFoundException;
import com.bsupply.productdashboard.factory.ProductResponseFactory;
import com.bsupply.productdashboard.repository.ProductCategoryRepository;
import com.bsupply.productdashboard.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    @CacheEvict(
            value = {"products", "productById"},
            allEntries = true)
    public void addProduct(ProductRequest productRequest) {

        ProductCategory productCategory = getProductCategory(productRequest);

        checkIfProductNameExist(productRequest.name());
        log.info("Adding product with details: {}", productRequest);

        Product product = new Product();
        product.setProductCategory(productCategory);
        product.setDescription(productRequest.description());
        product.setName(productRequest.name());
        product.setMeasurementUnits(productRequest.measurementUnits());
        product.setQuantityPerPack(productRequest.quantityPerPack());
        product.setWeight(productRequest.weight());
        double weightPerPackInKg = calculateWeightPerPackInKg(productRequest.weight(), productRequest.measurementUnits());
        product.setPackWeightInKg(weightPerPackInKg);
        productRepository.save(product);
    }

    @Cacheable(value = "productById")
    public ProductResponse getProductById(UUID productId) {

        log.info("Fetch product with id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));
        ProductCategory category = product.getProductCategory();
        ProductCategoryResponse productCategoryResponse =
                new ProductCategoryResponse(category.getId(), category.getName(), category.getDescription());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getQuantityPerPack(),
                product.getMeasurementUnits(),
                product.getPackWeightInKg(),
                productCategoryResponse
        );
    }

    @Cacheable(value = "products")
    public PageResponseDto<ProductResponse> getAllProducts(Pageable pageable) {

        log.info("Fetch all products");
        Page<Product> products = productRepository.findAll(pageable);
        return PageResponseDto.wrapResponse(products);
    }

    private double calculateWeightPerPackInKg(int weight, MeasurementUnits measurementUnits) {

        return MeasurementUnits.KILOGRAM == measurementUnits ? weight : (double) weight / 1_000;
    }

    private void checkIfProductNameExist(String productName) {

        if (productRepository.existsByName(productName)) {
            throw new DuplicateProductNameException(productName);
        }
    }

    @Transactional
    @CacheEvict(
            value = {"products", "productById"},
            allEntries = true)
    public ProductResponse updateProduct(UUID productId, ProductRequest productRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        ProductCategory productCategory = getProductCategory(productRequest);

        log.info("Update product with details {}", productRequest);
        product.setProductCategory(productCategory);
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setWeight(productRequest.weight());
        product.setQuantityPerPack(productRequest.quantityPerPack());
        product.setMeasurementUnits(productRequest.measurementUnits());
        double weightPerPackInKg = calculateWeightPerPackInKg(productRequest.weight(), productRequest.measurementUnits());
        product.setPackWeightInKg(weightPerPackInKg);
        Product saved = productRepository.save(product);
        return ProductResponseFactory.getProductResponse(saved);
    }

    @CacheEvict(
            value = {"products", "productById"},
            allEntries = true)
    public void deleteProduct(UUID productId) {

        log.info("Delete product with id: {}", productId);
        productRepository.deleteById(productId);
    }

    private ProductCategory getProductCategory(ProductRequest productRequest) {
        return productCategoryRepository.findById(productRequest.productCategoryId())
                .orElseThrow(() ->
                        new ProductCategoryNotFoundException(productRequest.productCategoryId().toString()));

    }
}
