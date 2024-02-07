package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.dto.PageResponseDto;
import com.bsupply.productdashboard.dto.request.ProductRequest;
import com.bsupply.productdashboard.dto.response.ProductResponse;
import com.bsupply.productdashboard.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody ProductRequest productRequest) {

        productService.addProduct(productRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID productId) {

        ProductResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "200") int pageSize,
            @RequestParam(required = false) Optional<String> productName
    ) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (productName.isPresent() && !productName.get().isBlank()) {
            PageResponseDto<ProductResponse> products = productService
                    .getByNameLike(productName.get(), pageable);
            return ResponseEntity.ok(products);
        }

        PageResponseDto<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductRequest productRequest) {

        ProductResponse product = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {

        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
