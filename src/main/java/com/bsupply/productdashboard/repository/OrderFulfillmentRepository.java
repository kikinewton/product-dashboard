package com.bsupply.productdashboard.repository;

import com.bsupply.productdashboard.entity.OrderFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderFulfillmentRepository extends JpaRepository<OrderFulfillment, UUID> {
    List<OrderFulfillment> findByProductOrderId(UUID id);
}