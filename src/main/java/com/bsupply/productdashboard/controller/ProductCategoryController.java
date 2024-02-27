package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.dto.PageResponseDto;
import com.bsupply.productdashboard.dto.request.ProductCategoryRequest;
import com.bsupply.productdashboard.dto.response.ProductCategoryResponse;
import com.bsupply.productdashboard.service.ProductCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/productCategories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPERATIONS')")
    public ResponseEntity<Void> addProductCategory(@RequestBody ProductCategoryRequest productCategoryRequest) {

        productCategoryService.addProductCategory(productCategoryRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductCategoryResponse>> getAllProductCategories(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "200") int pageSize
    ) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        PageResponseDto<ProductCategoryResponse> productCategories = productCategoryService.getProductCategories(pageable);
        return ResponseEntity.ok(productCategories);
    }

    @GetMapping("/{productCategoryId}")
    public ResponseEntity<ProductCategoryResponse> getProductCategoryById(@PathVariable UUID productCategoryId) {

        ProductCategoryResponse productCategory = productCategoryService.getProductCategoryById(productCategoryId);
        return ResponseEntity.ok(productCategory);
    }

    @PutMapping("/{productCategoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPERATIONS')")
    public ResponseEntity<ProductCategoryResponse> updateProductCategory(
            @PathVariable UUID productCategoryId,
            @RequestBody @Valid ProductCategoryRequest productCategoryRequest) {

        ProductCategoryResponse productCategoryResponse = productCategoryService
                .updateProductCategory(productCategoryId, productCategoryRequest);
        return ResponseEntity.ok(productCategoryResponse);
    }

    @DeleteMapping("/{productCategoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPERATIONS')")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable UUID productCategoryId) {

        productCategoryService.deleteProductCategory(productCategoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
