package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.PageResponseDto;
import com.bsupply.productdashboard.dto.request.ProductOrderRequest;
import com.bsupply.productdashboard.dto.response.ProductOrderResponse;
import com.bsupply.productdashboard.entity.Airline;
import com.bsupply.productdashboard.entity.Customer;
import com.bsupply.productdashboard.entity.Product;
import com.bsupply.productdashboard.entity.ProductOrder;
import com.bsupply.productdashboard.exception.AirlineNotFoundException;
import com.bsupply.productdashboard.exception.CustomerNotFoundException;
import com.bsupply.productdashboard.exception.ProductNotFoundException;
import com.bsupply.productdashboard.exception.ProductOrderNotFoundException;
import com.bsupply.productdashboard.factory.ProductOrderResponseFactory;
import com.bsupply.productdashboard.repository.AirlineRepository;
import com.bsupply.productdashboard.repository.CustomerRepository;
import com.bsupply.productdashboard.repository.ProductOrderRepository;
import com.bsupply.productdashboard.repository.ProductRepository;
import com.bsupply.productdashboard.specification.GenericSpecification;
import com.bsupply.productdashboard.specification.SearchCriteria;
import com.bsupply.productdashboard.specification.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private final AirlineRepository airlineRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final ProductOrderRepository productOrderRepository;




    public void addProductOrder(ProductOrderRequest productOrderRequest) {

        Airline airline = airlineRepository.findById(productOrderRequest.airlineId())
                .orElseThrow(() -> new AirlineNotFoundException(productOrderRequest.airlineId().toString()));

        Customer customer = customerRepository.findById(productOrderRequest.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(productOrderRequest.customerId().toString()));

        Product product = productRepository.findById(productOrderRequest.productId())
                .orElseThrow(() -> new ProductNotFoundException(productOrderRequest.productId().toString()));

        log.info("Add new product order {}", productOrderRequest);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProduct(product);
        productOrder.setCustomer(customer);
        productOrder.setAirline(airline);
        productOrder.setQuantity(productOrderRequest.quantity());
        productOrder.setFlight(productOrderRequest.flight());
        productOrder.setDescription(productOrderRequest.description());
        productOrderRepository.save(productOrder);
    }

    public ProductOrderResponse getProductOrderById(UUID productOrderId) {

        log.info("Find product order by id {}", productOrderId);
        ProductOrder productOrder = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new ProductOrderNotFoundException(productOrderId.toString()));

        return ProductOrderResponseFactory.getProductOrderResponse(productOrder);
    }

    public PageResponseDto<ProductOrderResponse> getProductOrders(Pageable pageable) {

        log.info("Fetch all product orders");
        Page<ProductOrderResponse> result = productOrderRepository.findAll(pageable)
                .map(p -> ProductOrderResponseFactory.getProductOrderResponse(p));
        return PageResponseDto.wrapResponse(result);
    }

    public PageResponseDto<ProductOrderResponse> getProductOrdersByCustomer(UUID customerId, Pageable pageable) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
        log.info("Fetch all product orders by customer {}", customerId);

        GenericSpecification<ProductOrder> productOrderSpecification = new GenericSpecification<>();
        productOrderSpecification.add(new SearchCriteria("customer", customer, SearchOperation.EQUAL));
        Page<ProductOrderResponse> result = productOrderRepository.findAll(productOrderSpecification, pageable)
                .map(p -> ProductOrderResponseFactory.getProductOrderResponse(p));
        return PageResponseDto.wrapResponse(result);
    }
}
