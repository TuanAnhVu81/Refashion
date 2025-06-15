package exe201.Refashion.service;

import exe201.Refashion.dto.request.OrderItemRequest;
import exe201.Refashion.dto.request.OrderRequest;
import exe201.Refashion.dto.response.OrderHistoryResponse;
import exe201.Refashion.dto.response.OrderResponse;
import exe201.Refashion.entity.OrderItems;
import exe201.Refashion.entity.Orders;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.enums.OrderImageType;
import exe201.Refashion.enums.OrderStatus;
import exe201.Refashion.enums.PaymentStatus;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.OrderMapper;
import exe201.Refashion.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    OrderMapper orderMapper;
    OrderItemsRepository orderItemsRepository;
    EmailService emailService;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.info("Creating order with request: {}", orderRequest);

        // Validate buyer
        Users buyer = userRepository.findById(orderRequest.getBuyerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy danh sách sản phẩm
        List<Products> products = orderRequest.getItems().stream()
                .map(item -> productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)))
                .toList();

        if (products.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // Validate: tất cả sản phẩm cùng 1 seller
        Users seller = products.get(0).getSeller();
        boolean sameSeller = products.stream()
                .allMatch(product -> product.getSeller().getId().equals(seller.getId()));
        if (!sameSeller) {
            throw new AppException(ErrorCode.MULTIPLE_SELLERS_NOT_ALLOWED);
        }

        // Validate sellerId
        if (!orderRequest.getSellerId().equals(seller.getId())) {
            throw new AppException(ErrorCode.SELLER_MISMATCH);
        }

        // Tính totalAmount
        BigDecimal totalAmount = products.stream()
                .map(product -> {
                    Integer quantity = orderRequest.getItems().stream()
                            .filter(item -> item.getProductId().equals(product.getId()))
                            .findFirst()
                            .map(OrderItemRequest::getQuantity)
                            .orElse(1);
                    return product.getPrice().multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tạo order trước (chưa gán images/items)
        Orders order = Orders.builder()
                .buyer(buyer)
                .seller(seller)
                .totalAmount(totalAmount)
                .shippingAddress(orderRequest.getShippingAddress())
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // Save order để lấy ID
        Orders savedOrder = orderRepository.saveAndFlush(order);
        log.info("Order saved with ID: {}", savedOrder.getId());

        // Thêm payment images nếu có
        if (orderRequest.getPaymentImageUrls() != null && !orderRequest.getPaymentImageUrls().isEmpty()) {
            String firstUrl = orderRequest.getPaymentImageUrls().stream()
                    .filter(url -> url != null && !url.trim().isEmpty())
                    .findFirst()
                    .orElse(null);
            savedOrder.setPaymentScreenshotUrl(firstUrl);
        }
        orderRepository.save(savedOrder);
        log.info("Order: {}", savedOrder);
        log.info("Order Items: {}", savedOrder.getOrderItems());

        // Tạo và lưu order items
        List<OrderItems> orderItems = products.stream()
                .map(product -> {
                    Integer quantity = orderRequest.getItems().stream()
                            .filter(item -> item.getProductId().equals(product.getId()))
                            .findFirst()
                            .map(OrderItemRequest::getQuantity)
                            .orElse(1);
                    OrderItems item = OrderItems.builder()
                            .order(savedOrder)
                            .product(product)
                            .quantity(quantity)
                            .priceAtPurchase(product.getPrice())
                            .build();
                    return item;
                })
                .toList();

        log.info("Order: {}", savedOrder.getId());
        log.info("Order Items: {}", savedOrder.getOrderItems());

        savedOrder.setOrderItems(orderItems);
        orderItemsRepository.saveAll(orderItems);

        log.info("Order: {}", savedOrder.getId());
        log.info("Order Items: {}", savedOrder.getOrderItems());

        orderItemsRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);
        log.info("Saved {} order items", orderItems.size());

        // Đánh dấu sản phẩm đã bán
        for (Products product : products) {
            product.setIsSold(true);
        }
        productRepository.saveAll(products);
        log.info("Marked {} products as sold", products.size());

        // Trả về response
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Transactional
    public void cancelOrder(String orderId, String buyerId) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public List<OrderResponse> getAllOrders() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderHistoryResponse getOrderHistory(String buyerId) {
        List<Orders> orders = orderRepository.findByBuyerId(buyerId);
        List<OrderResponse> orderResponses = orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
        return new OrderHistoryResponse(orderResponses);
    }

    public OrderResponse getOrderStatus(String orderId, String buyerId) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(String orderId, String status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.setStatus(orderStatus);
        order = orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersBySeller(String sellerId) {
        userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Orders> orders = orderRepository.findBySellerId(sellerId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(String orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse updatePaymentStatus(String orderId, String paymentStatus) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        PaymentStatus newPaymentStatus;
        try {
            newPaymentStatus = PaymentStatus.valueOf(paymentStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_PAYMENT_STATUS);
        }

        order.setPaymentStatus(newPaymentStatus);
        order = orderRepository.save(order);

        // Send email notifications based on payment status
        try {
            if (newPaymentStatus == PaymentStatus.PAID) {
                // Notify seller
                emailService.sendOrderPaidEmail(order.getSeller().getEmail(), orderId, order.getTotalAmount().toString(), order.getShippingAddress());
                // Notify buyer
                emailService.sendOrderPaidConfirmationToBuyer(order.getBuyer().getEmail(), orderId, order.getTotalAmount().toString());
            } else if (newPaymentStatus == PaymentStatus.UNPAID) {
                emailService.sendOrderUnpaidEmail(order.getBuyer().getEmail(), orderId, order.getTotalAmount().toString());
            }
        } catch (MessagingException e) {
            log.error("Failed to send email notification for order {}: {}", orderId, e.getMessage());
        }

        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByProductId(String productId) {
        List<Orders> orders = orderRepository.findByProductId(productId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updatePaymentImage(String orderId, String buyerId, List<String> paymentImageUrls) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (paymentImageUrls != null && !paymentImageUrls.isEmpty()) {
            String newImageUrl = paymentImageUrls.get(0);
            if (newImageUrl != null && !newImageUrl.trim().isEmpty()) {
                order.setPaymentScreenshotUrl(newImageUrl);
            }
        }

        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse updateToShipped(String orderId, List<String> packageImageUrls, String sellerId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getSeller().getId().equals(sellerId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        if (packageImageUrls != null && !packageImageUrls.isEmpty()) {
            order.setSellerPackageImageUrl(packageImageUrls.get(0));
        } else {
            order.setSellerPackageImageUrl(null);
        }

        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse uploadAdminPaymentImage(String orderId, List<String> imageUrls) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (imageUrls != null && !imageUrls.isEmpty()) {
            String adminImage = imageUrls.get(0);
            if (adminImage != null && !adminImage.trim().isEmpty()) {
                order.setAdminPaymentScreenshotUrl(adminImage);
            }
        }

        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse markOrderAsRefund(String orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setStatus(OrderStatus.REFUND);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse confirmDelivered(String orderId, List<String> packageImageUrls, String buyerId) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (packageImageUrls != null && !packageImageUrls.isEmpty()) {
            order.setBuyerPackageImageUrl(packageImageUrls.get(0));
        } else {
            order.setBuyerPackageImageUrl(null);
        }

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }
}