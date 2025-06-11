package exe201.Refashion.service;

import exe201.Refashion.dto.request.OrderItemRequest;
import exe201.Refashion.dto.request.OrderRequest;
import exe201.Refashion.dto.response.OrderHistoryResponse;
import exe201.Refashion.dto.response.OrderResponse;
import exe201.Refashion.entity.OrderItems;
import exe201.Refashion.enums.OrderStatus;
import exe201.Refashion.entity.Orders;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.enums.PaymentStatus;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.OrderMapper;
import exe201.Refashion.repository.OrderItemsRepository;
import exe201.Refashion.repository.OrderRepository;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static exe201.Refashion.enums.PaymentStatus.UNPAID;

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
    FileUploadService fileUploadService;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
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

        // Validate: tất cả sản phẩm phải cùng 1 seller
        Users seller = products.get(0).getSeller();
        boolean sameSeller = products.stream()
                .allMatch(product -> product.getSeller().getId().equals(seller.getId()));

        if (!sameSeller) {
            throw new AppException(ErrorCode.MULTIPLE_SELLERS_NOT_ALLOWED);
        }

        // Tính totalAmount
        BigDecimal totalAmount = orderRequest.getItems().stream()
                .map(item -> {
                    Products product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity() != null ? item.getQuantity() : 1));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Orders order = Orders.builder()
                .id(UUID.randomUUID().toString())
                .buyer(buyer)
                .seller(seller)
                .totalAmount(totalAmount)
                .shippingAddress(orderRequest.getShippingAddress())
                .status(OrderStatus.PENDING)
                .paymentStatus(UNPAID)
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu order trước
        Orders savedOrder = orderRepository.save(order);

        // Tạo và lưu OrderItems sau khi đã có Orders
        List<OrderItems> orderItems = orderRequest.getItems().stream()
                .map(item -> {
                    Products product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                    return OrderItems.builder()
                            .id(UUID.randomUUID().toString()) // Tạo ID cho OrderItems
                            .order(savedOrder) // Gán order đã lưu
                            .product(product)
                            .quantity(item.getQuantity() != null ? item.getQuantity() : 1)
                            .priceAtPurchase(product.getPrice())
                            .build();
                })
                .collect(Collectors.toList());
        orderItemsRepository.saveAll(orderItems);

        // Cập nhật orderItems cho savedOrder và lưu lại
        savedOrder.setOrderItems(orderItems);
        orderRepository.save(savedOrder); // Lưu lại để cập nhật quan hệ

        // Đánh dấu là sold nếu đơn hàng đã được lưu thành công
        Products product = productRepository.findById(orderRequest.getItems().getFirst().getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setIsSold(true);
        productRepository.save(product);

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Transactional
    public void cancelOrder(String orderId, String buyerId) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }
        order.setStatus(OrderStatus.CANCELED);
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

        validateStatusTransition(order.getStatus(), orderStatus);

        order.setStatus(orderStatus);
        order = orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersBySeller(String sellerId) {
        // Kiểm tra xem seller có tồn tại không
        userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy danh sách đơn hàng của seller
        List<Orders> orders = orderRepository.findBySellerId(sellerId);

        // Ánh xạ sang OrderResponse
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

        validatePaymentStatusTransition(order.getPaymentStatus(), newPaymentStatus);

        order.setPaymentStatus(newPaymentStatus);
        order = orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByProductId(String productId) {
        List<Orders> orders = orderRepository.findByProductId(productId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updatePaymentStatusWithImage(String orderId, String paymentStatus, MultipartFile paymentImage) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        PaymentStatus newPaymentStatus = PaymentStatus.valueOf(paymentStatus.toUpperCase());
        validatePaymentStatusTransition(order.getPaymentStatus(), newPaymentStatus);

        if (paymentImage != null) {
            String imageUrl = fileUploadService.uploadImage(paymentImage);
            order.setPaymentScreenshotUrl(imageUrl);
        }
        order.setPaymentStatus(newPaymentStatus);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse updateToShipped(String orderId, MultipartFile packageImage, String sellerId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getSeller().getId().equals(sellerId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }
        validateStatusTransition(order.getStatus(), OrderStatus.SHIPPED);
        if (packageImage != null) {
            String imageUrl = fileUploadService.uploadImage(packageImage);
            order.setSellerPackageImageUrl(imageUrl);
        }
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse confirmDelivered(String orderId, MultipartFile packageImage, String buyerId) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        validateStatusTransition(order.getStatus(), OrderStatus.DELIVERED);
        if (packageImage != null) {
            String imageUrl = fileUploadService.uploadImage(packageImage);
            order.setBuyerPackageImageUrl(imageUrl);
        }
        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                if (newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELED) {
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
                }
                break;
            case PROCESSING:
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELED) {
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
                }
                break;
            case SHIPPED:
                if (newStatus != OrderStatus.DELIVERED && newStatus != OrderStatus.CANCELED) {
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
                }
                break;
            case DELIVERED:
            case CANCELED:
                throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            default:
                throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }

    private void validatePaymentStatusTransition(PaymentStatus currentStatus, PaymentStatus newStatus) {
        switch (currentStatus) {
            case UNPAID:
                if (newStatus != PaymentStatus.PAID) {
                    throw new AppException(ErrorCode.INVALID_PAYMENT_STATUS_TRANSITION);
                }
                break;
            case PAID:
                if (newStatus != PaymentStatus.REFUNDED) {
                    throw new AppException(ErrorCode.INVALID_PAYMENT_STATUS_TRANSITION);
                }
                break;
            case REFUNDED:
                throw new AppException(ErrorCode.INVALID_PAYMENT_STATUS_TRANSITION);
            default:
                throw new AppException(ErrorCode.INVALID_PAYMENT_STATUS);
        }
    }
}