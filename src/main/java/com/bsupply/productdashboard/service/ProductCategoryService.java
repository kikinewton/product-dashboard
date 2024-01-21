package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.PageResponseDto;
import com.bsupply.productdashboard.dto.request.ProductCategoryRequest;
import com.bsupply.productdashboard.dto.response.ProductCategoryResponse;
import com.bsupply.productdashboard.entity.ProductCategory;
import com.bsupply.productdashboard.exception.DuplicateProductCategoryNameException;
import com.bsupply.productdashboard.exception.ProductCategoryNotFoundException;
import com.bsupply.productdashboard.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public void addProductCategory(ProductCategoryRequest productCategoryRequest) {

        checkProductCategoryNameExist(productCategoryRequest.name());
        log.info("Add product category with {}", productCategoryRequest);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(productCategoryRequest.name());
        productCategory.setDescription(productCategoryRequest.description());
        productCategoryRepository.save(productCategory);
    }

    public ProductCategoryResponse getProductCategoryById(UUID productCategoryId) {

        log.info("Fetch product category with id: %s".formatted(productCategoryId));
        return productCategoryRepository
                .findById(productCategoryId)
                .map(p -> new ProductCategoryResponse(p.getId(), p.getName(), p.getDescription()))
                .orElseThrow(() -> new ProductCategoryNotFoundException(productCategoryId.toString()));
    }

    public PageResponseDto<ProductCategoryResponse> getProductCategories(Pageable pageable) {

        log.info("Fetch all product categories");
        Page<ProductCategoryResponse> result = productCategoryRepository
                .findAll(pageable)
                .map(p -> new ProductCategoryResponse(p.getId(), p.getName(), p.getDescription()));
        return PageResponseDto.wrapResponse(result);
    }

    public ProductCategoryResponse updateProductCategory(UUID productCategoryId,
                                                         ProductCategoryRequest productCategoryRequest) {

        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ProductCategoryNotFoundException(productCategoryId.toString()));
        log.info("Update product category with id: {}", productCategoryId);

        productCategory.setName(productCategoryRequest.name());
        productCategory.setDescription(productCategoryRequest.description());
        ProductCategory updatedCategory = productCategoryRepository.save(productCategory);
        return new ProductCategoryResponse(
                updatedCategory.getId(),
                updatedCategory.getName(),
                updatedCategory.getDescription());
    }

    public void deleteProductCategory(UUID productCategoryId) {

        log.info("Attempt to delete product category with id: {}", productCategoryId);
        productCategoryRepository.deleteById(productCategoryId);
        log.info("Product category with id: {} deleted", productCategoryId);
    }

    private void checkProductCategoryNameExist(String productCategoryName) {
        if (productCategoryRepository.existsByName(productCategoryName)) {
            throw new DuplicateProductCategoryNameException(productCategoryName);
        }
    }
}
