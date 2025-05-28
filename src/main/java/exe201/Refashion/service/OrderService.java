package exe201.Refashion.service;

import exe201.Refashion.dto.request.OrderItemRequest;
import exe201.Refashion.dto.request.OrderRequest;
import exe201.Refashion.dto.response.OrderHistoryResponse;
import exe201.Refashion.dto.response.OrderResponse;
import exe201.Refashion.enums.OrderStatus;
import exe201.Refashion.entity.Orders;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.enums.PaymentStatus;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.OrderMapper;
import exe201.Refashion.repository.OrderRepository;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Users buyer = userRepository.findById(orderRequest.getBuyerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy amount(price) của từng sản phẩm rồi tính tổng
        List<Products> products = orderRequest.getItems().stream()
                .map(item -> productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)))
                .toList();
        BigDecimal totalAmount = products.stream()
                .map(Products::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Orders order = Orders.builder()
                .id(UUID.randomUUID().toString())
                .buyer(buyer)
                .totalAmount(totalAmount)
                .shippingAddress(orderRequest.getShippingAddress())
                .status(OrderStatus.PENDING)
                .paymentStatus(UNPAID)
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu order và các order items (giả sử logic lưu items đã được xử lý)
        Orders savedOrder = orderRepository.save(order);

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
}