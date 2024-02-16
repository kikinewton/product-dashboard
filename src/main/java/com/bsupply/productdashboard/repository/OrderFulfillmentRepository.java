package com.bsupply.productdashboard.repository;

import com.bsupply.productdashboard.dto.projection.OrderFulfillmentStatus;
import com.bsupply.productdashboard.entity.OrderFulfillment;
import com.bsupply.productdashboard.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderFulfillmentRepository extends JpaRepository<OrderFulfillment, UUID> {
    List<OrderFulfillment> findByProductOrderId(UUID id);

    @Query(value = """
            SELECT ofs.customer, ofs.product_order_id as productOrderId, ofs.product_id as productId, ofs.product, 
            ofs.total_ordered as totalOrdered, ofs.total_processed as totalProcessed, 
            ofs.remaining_to_process as remainingToProcess, ofs.days_to_deadline as daysToDeadline 
            FROM order_fulfillment_status ofs WHERE ofs.product_order_id = ?1 and ofs.product_id = ?2
                        """, nativeQuery = true)
    OrderFulfillmentStatus getFulfillmentStatusByOrderAndProductId(UUID productOrderId, UUID productId);

    @Query(value = """
            SELECT
            	CASE
            		WHEN SUM(od.remaining_to_process) = 0 THEN 'COMPLETED'
            		ELSE 'PENDING'
            	END AS order_status
            FROM
            	product_order po
            JOIN
                order_fulfillment_status od ON
            	po.id = od.product_order_id
            WHERE
            	po.id = ?1
            GROUP BY
            	po.id
            """, nativeQuery = true)
    OrderStatus findOrderStatusByOrderId(UUID productOrderId);

}