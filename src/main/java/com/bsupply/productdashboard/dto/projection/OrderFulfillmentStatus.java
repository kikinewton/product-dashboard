package com.bsupply.productdashboard.dto.projection;

import java.util.UUID;

public interface OrderFulfillmentStatus {

    String getCustomer();
    UUID getProductOrderId();
    UUID getProductId();
    String getProduct();
    int getTotalOrdered();
    int getTotalProcessed();
    int getRemainingToProcess();
    int getDaysToDeadline();

}
