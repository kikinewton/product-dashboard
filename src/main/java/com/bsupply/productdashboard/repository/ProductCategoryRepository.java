package com.bsupply.productdashboard.repository;

import com.bsupply.productdashboard.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

    boolean existsByName(String name);

}