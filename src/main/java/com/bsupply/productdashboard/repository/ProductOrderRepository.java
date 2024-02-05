package com.bsupply.productdashboard.repository;

import com.bsupply.productdashboard.entity.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, UUID>, JpaSpecificationExecutor<ProductOrder> {
    Page<ProductOrder> findByCustomerId(UUID id, Pageable pageable);

    boolean existsByOrderDetailProductId(UUID id);

}