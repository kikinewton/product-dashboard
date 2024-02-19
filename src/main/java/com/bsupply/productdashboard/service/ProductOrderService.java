package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.PageResponseDto;
import com.bsupply.productdashboard.dto.request.OrderDetailRequest;
import com.bsupply.productdashboard.dto.request.OrderFulfillmentRequest;
import com.bsupply.productdashboard.dto.request.ProductAndQuantityDto;
import com.bsupply.productdashboard.dto.request.ProductOrderRequest;
import com.bsupply.productdashboard.dto.response.ProductOrderResponse;
import com.bsupply.productdashboard.entity.Airline;
import com.bsupply.productdashboard.entity.Customer;
import com.bsupply.productdashboard.entity.OrderDetail;
import com.bsupply.productdashboard.entity.OrderFulfillment;
import com.bsupply.productdashboard.entity.Product;
import com.bsupply.productdashboard.entity.ProductOrder;
import com.bsupply.productdashboard.enums.OrderStatus;
import com.bsupply.productdashboard.exception.AirlineNotFoundException;
import com.bsupply.productdashboard.exception.CustomerNotFoundException;
import com.bsupply.productdashboard.exception.ProductNotFoundException;
import com.bsupply.productdashboard.exception.ProductOrderFulfillmentException;
import com.bsupply.productdashboard.exception.ProductOrderNotFoundException;
import com.bsupply.productdashboard.factory.ProductOrderResponseFactory;
import com.bsupply.productdashboard.repository.AirlineRepository;
import com.bsupply.productdashboard.repository.CustomerRepository;
import com.bsupply.productdashboard.repository.OrderDetailRepository;
import com.bsupply.productdashboard.repository.OrderFulfillmentRepository;
import com.bsupply.productdashboard.repository.ProductOrderRepository;
import com.bsupply.productdashboard.repository.ProductRepository;
import com.bsupply.productdashboard.specification.GenericSpecification;
import com.bsupply.productdashboard.specification.SearchCriteria;
import com.bsupply.productdashboard.specification.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private final AirlineRepository airlineRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final ProductOrderRepository productOrderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final OrderFulfillmentRepository orderFulfillmentRepository;


    @CacheEvict(
            value = {"productOrders", "productOrderById", "fulfillments", "productOrdersByCustomerId"},
            allEntries = true
    )
    @Transactional
    public void addProductOrder(ProductOrderRequest productOrderRequest) {

        Airline airline = airlineRepository.findById(productOrderRequest.airlineId())
                .orElseThrow(() -> new AirlineNotFoundException(productOrderRequest.airlineId().toString()));

        Customer customer = customerRepository.findById(productOrderRequest.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(productOrderRequest.customerId().toString()));

        List<OrderDetail> orderDetails = createOrderDetails(productOrderRequest.products());

        log.info("Add new product order {}", productOrderRequest);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setOrderDetail(orderDetails);
        productOrder.setCustomer(customer);
        productOrder.setAirline(airline);
        productOrder.setRequiredDate(productOrderRequest.requiredDate().toInstant());
        productOrder.setFlight(productOrderRequest.flight());

        String description = productOrderRequest.description().isBlank()
                ? "Order by %s on %s".formatted(customer.getName(), LocalDate.now().toString())
                : productOrderRequest.description();

        productOrder.setDescription(description);
        productOrderRepository.save(productOrder);
    }

    @Cacheable(value = "productOrderById")
    public ProductOrderResponse getProductOrderById(UUID productOrderId) {

        log.info("Find product order by id {}", productOrderId);
        ProductOrder productOrder = getProductOrder(productOrderId);

        log.info("Find associated fulfillment to order: {}", productOrderId);
        List<OrderFulfillment> fulfillment = getFulfillmentForOrder(productOrderId);

        return ProductOrderResponseFactory.getProductOrderResponse(productOrder, fulfillment);
    }

    @Cacheable(value = "fulfillments")
    private List<OrderFulfillment> getFulfillmentForOrder(UUID productOrderId) {
        return orderFulfillmentRepository.findByProductOrderId(productOrderId);
    }

    @Cacheable
    private ProductOrder getProductOrder(UUID productOrderId) {
        ProductOrder productOrder = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new ProductOrderNotFoundException(productOrderId.toString()));
        return productOrder;
    }

    @Cacheable(value = "productOrders")
    public PageResponseDto<ProductOrderResponse> getPendingProductOrders(Pageable pageable) {

        log.info("Fetch all product orders");
        GenericSpecification<ProductOrder> productOrderSpecification = new GenericSpecification<>();
        productOrderSpecification.add(new SearchCriteria("status", OrderStatus.PENDING.name(), SearchOperation.EQUAL));
        Page<ProductOrderResponse> result = productOrderRepository.findAll(productOrderSpecification, pageable)
                .map(p -> {
                    List<OrderFulfillment> fulfillmentForOrder = getFulfillmentForOrder(p.getId());
                    return ProductOrderResponseFactory.getProductOrderResponse(p, fulfillmentForOrder);
                });
        return PageResponseDto.wrapResponse(result);
    }

    public PageResponseDto<ProductOrderResponse> getProductOrdersBetweenDates(
            Date startPeriod,
            Date endPeriod,
            Pageable pageable) {

        GenericSpecification<ProductOrder> productOrderSpecification = new GenericSpecification<>();
        productOrderSpecification.add(new SearchCriteria("createdAt", startPeriod, SearchOperation.GREATER_THAN_EQUAL));
        productOrderSpecification.add(new SearchCriteria("createdAt", endPeriod, SearchOperation.LESS_THAN_EQUAL));
        Page<ProductOrderResponse> productOrders = productOrderRepository.findAll(productOrderSpecification, pageable)
                .map(p -> {
                    List<OrderFulfillment> fulfillmentForOrder = getFulfillmentForOrder(p.getId());
                    return ProductOrderResponseFactory.getProductOrderResponse(p, fulfillmentForOrder);
                });
        return PageResponseDto.wrapResponse(productOrders);
    }

    @Cacheable(value = "productOrdersByCustomerId")
    public PageResponseDto<ProductOrderResponse> getProductOrdersByCustomer(UUID customerId, Pageable pageable) {

        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
        log.info("Fetch all product orders by customer {}", customerId);
        Page<ProductOrderResponse> result = productOrderRepository
                .findByCustomerId(customerId, pageable)
                .map(p -> {
                    List<OrderFulfillment> fulfillmentForOrder = getFulfillmentForOrder(p.getId());
                    return ProductOrderResponseFactory.getProductOrderResponse(p, fulfillmentForOrder);
                });

        return PageResponseDto.wrapResponse(result);
    }

    private List<OrderDetail> createOrderDetails(Set<ProductAndQuantityDto> productsAndQuantities) {
        Map<UUID, Integer> productQuantityMap = getProductQuantityMap(productsAndQuantities);
        List<Product> products = fetchProducts(productsAndQuantities);

        List<OrderDetail> orderDetails = products.stream()
                .map(product -> createOrderDetail(product, productQuantityMap))
                .collect(Collectors.toList());

        return orderDetailRepository.saveAll(orderDetails);
    }

    private Map<UUID, Integer> getProductQuantityMap(Set<ProductAndQuantityDto> productsAndQuantities) {
        return productsAndQuantities.stream()
                .collect(Collectors.toMap(
                        ProductAndQuantityDto::productId,
                        ProductAndQuantityDto::quantity
                ));
    }

    private List<Product> fetchProducts(Set<ProductAndQuantityDto> productsAndQuantities) {
        return productsAndQuantities.stream()
                .map(ProductAndQuantityDto::productId)
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> new ProductNotFoundException(productId.toString())))
                .collect(Collectors.toList());
    }

    private OrderDetail createOrderDetail(Product product, Map<UUID, Integer> productQuantityMap) {
        int quantity = productQuantityMap.getOrDefault(product.getId(), 0);
        return OrderDetail.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }

    @CacheEvict(
            value = {"productOrders", "productOrderById", "fulfillments", "productOrdersByCustomerId"},
            allEntries = true)
    @Transactional
    public void orderFulfillment(UUID productOrderId, OrderFulfillmentRequest orderFulfillmentRequest) {
        log.info("Fulfillment of order with id: {}", productOrderId);
        checkOrderFulfillmentStatus(productOrderId);

        // Retrieve the product order
        ProductOrder productOrder = getProductOrder(productOrderId);

        // Create and set order fulfillments
        List<OrderFulfillment> orderFulfillments = orderFulfillmentRequest.orderDetails()
                .stream()
                .map(orderDetail -> {
                    Product product = productRepository.findById(orderDetail.productId())
                            .orElseThrow(() -> new ProductNotFoundException(orderDetail.productId().toString()));

                    OrderFulfillment orderFulfillment = new OrderFulfillment();
                    orderFulfillment.setProductOrder(productOrder);
                    orderFulfillment.setProduct(product);
                    orderFulfillment.setQuantity(orderDetail.quantity());
                    return orderFulfillment;
                })
                .collect(Collectors.toList());

//        orderFulfillments.stream().map(o -> orderFulfillmentRepository
//                .getFulfillmentStatusByOrderAndProductId(productOrderId, o.getProduct().getId()));

        // Save all order fulfillments
        orderFulfillmentRepository.saveAll(orderFulfillments);
        processOrderStatus(productOrderId, productOrder);
    }

    private void processOrderStatus(UUID productOrderId, ProductOrder productOrder) {

        log.info("Check if status of order: {} is COMPLETED", productOrderId);
        OrderStatus status = orderFulfillmentRepository.findOrderStatusByOrderId(productOrderId);
        log.info("Order status is {}", status);
        if (OrderStatus.COMPLETED == status) {
            productOrder.setStatus(status);
            productOrderRepository.save(productOrder);
            log.info("Order {} is COMPLETED", productOrderId);
        }
    }


    private void checkOrderFulfillmentStatus(UUID productOrderId) {

        log.info("Check status of order");
        ProductOrder productOrder = getProductOrder(productOrderId);

        if (productOrder.getOrderDetail().isEmpty() || productOrder.getStatus() != OrderStatus.PENDING) {
            throw new ProductOrderFulfillmentException(productOrderId.toString());
        }
    }

    @CacheEvict(
            value = {"productOrders", "productOrderById", "fulfillments", "productOrdersByCustomerId"},
            allEntries = true
    )
    @Transactional
    public void updateOrder(UUID productOrderId, ProductOrderRequest productOrderRequest) {

        Airline airline = airlineRepository.findById(productOrderRequest.airlineId())
                .orElseThrow(() -> new AirlineNotFoundException(productOrderRequest.airlineId().toString()));

        Customer customer = customerRepository.findById(productOrderRequest.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(productOrderRequest.customerId().toString()));

        log.info("Update order with id {}", productOrderId);
        ProductOrder productOrder = getProductOrder(productOrderId);
        productOrder.setAirline(airline);
        productOrder.setCustomer(customer);
        productOrder.setRequiredDate(productOrderRequest.requiredDate().toInstant());
        productOrder.setFlight(productOrderRequest.flight());
        productOrder.setDescription(productOrderRequest.description());
        productOrderRepository.save(productOrder);
    }


    @CacheEvict(
            value = {"productOrders", "productOrderById", "fulfillments", "productOrdersByCustomerId"},
            allEntries = true
    )
    @Transactional
    public void updateOrderDetailById(UUID orderDetailId, OrderDetailRequest orderDetailRequest) {

        log.info("Update order detail with id {}", orderDetailId);
        Optional<OrderDetail> detailOptional = orderDetailRepository.findById(orderDetailId);
        detailOptional.ifPresent(od -> {

            log.info("Change quantity from {} to {}", od.getQuantity(), orderDetailRequest.quantity());
            od.setQuantity(orderDetailRequest.quantity());
            orderDetailRepository.save(od);
        });
    }


    @CacheEvict(
            value = {"productOrders", "productOrderById", "fulfillments", "productOrdersByCustomerId"},
            allEntries = true
    )
    @Transactional
    public void updateFulfillmentById(UUID orderFulfillmentId, OrderDetailRequest orderDetailRequest) {

        log.info("Update fulfillment with id {}", orderFulfillmentId);
        Optional<OrderFulfillment> fulfillmentOptional = orderFulfillmentRepository.findById(orderFulfillmentId);
        fulfillmentOptional.ifPresent(f -> {

            log.info("Change quantity from {} to {}", f.getQuantity(), orderDetailRequest.quantity());
            f.setQuantity(orderDetailRequest.quantity());
            orderFulfillmentRepository.save(f);
        });
    }
}
