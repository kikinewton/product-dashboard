package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.dto.PageResponseDto;
import com.bsupply.productdashboard.dto.request.OrderDetailRequest;
import com.bsupply.productdashboard.dto.request.OrderFulfillmentRequest;
import com.bsupply.productdashboard.dto.request.ProductOrderRequest;
import com.bsupply.productdashboard.dto.response.ProductOrderResponse;
import com.bsupply.productdashboard.service.ProductOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/api/v1/productOrders")
@RequiredArgsConstructor
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    @PostMapping
    public ResponseEntity<Void> addProductOrder(@RequestBody @Valid ProductOrderRequest productOrderRequest) {

        productOrderService.addProductOrder(productOrderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductOrderResponse>> getProductOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "600") int pageSize
    ) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        PageResponseDto<ProductOrderResponse> productOrders = productOrderService.getPendingProductOrders(pageable);
        return ResponseEntity.ok(productOrders);
    }

    @GetMapping("/{productOrderId}")
    public ResponseEntity<ProductOrderResponse> getProductOrder(@PathVariable UUID productOrderId) {

        ProductOrderResponse productOrder = productOrderService.getProductOrderById(productOrderId);
        return ResponseEntity.ok(productOrder);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<PageResponseDto<ProductOrderResponse>> getProductOrderByCustomer(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "200") int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        PageResponseDto<ProductOrderResponse> productOrdersByCustomer =
                productOrderService.getProductOrdersByCustomer(customerId, pageable);
        return ResponseEntity.ok(productOrdersByCustomer);
    }

    @GetMapping("/history")
    public ResponseEntity<PageResponseDto<ProductOrderResponse>> getProductOrdersBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") Date startPeriod,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") Date endPeriod,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "600") int pageSize
    ) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        PageResponseDto<ProductOrderResponse> productOrders = productOrderService
                .getProductOrdersBetweenDates(startPeriod, endPeriod, pageable);
        return ResponseEntity.ok(productOrders);
    }

    @PostMapping("/{productOrderId}/fulfillment")
    public ResponseEntity<Void> orderFulfillment(
            @PathVariable UUID productOrderId,
            @Valid @RequestBody OrderFulfillmentRequest orderFulfillmentRequest) {

        productOrderService.orderFulfillment(productOrderId, orderFulfillmentRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productOrderId}")
    public ResponseEntity<ProductOrderResponse> updateProductOrder(
            @PathVariable UUID productOrderId,
            @RequestBody @Valid ProductOrderRequest productOrderRequest) {

        productOrderService.updateOrder(productOrderId, productOrderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productOrderId}/orderDetails/{orderDetailId}")
    public ResponseEntity<ProductOrderResponse> updateOrderDetails(
            @PathVariable UUID productOrderId,
            @PathVariable UUID orderDetailId,
            @RequestBody @Valid OrderDetailRequest orderDetailRequest) {

        log.info("Update details of order: {}", productOrderId);
        productOrderService.updateOrderDetailById(orderDetailId, orderDetailRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productOrderId}/orderFulfillments/{orderFulfillmentId}")
    public ResponseEntity<ProductOrderResponse> updateOrderFulfillment(
            @PathVariable UUID productOrderId,
            @PathVariable UUID orderFulfillmentId,
            @RequestBody @Valid OrderDetailRequest orderDetailRequest) {

        log.info("Update fulfillment of order: {}", productOrderId);
        productOrderService.updateFulfillmentById(orderFulfillmentId, orderDetailRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
