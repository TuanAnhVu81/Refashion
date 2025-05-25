package exe201.Refashion.service;

import exe201.Refashion.dto.request.OrderRequest;
import exe201.Refashion.dto.response.OrderHistoryResponse;
import exe201.Refashion.dto.response.OrderResponse;
import exe201.Refashion.entity.Orders;
import exe201.Refashion.entity.Users;
import exe201.Refashion.enums.OrderStatus;
import exe201.Refashion.repository.OrderRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Users buyer = userRepository.findById(orderRequest.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        if (buyer == null) throw new RuntimeException("Buyer not found");

        Orders order = Orders.builder()
                .id(UUID.randomUUID().toString())
                .buyer(buyer)
                .totalAmount(orderRequest.getTotalAmount())
                .shippingAddress(orderRequest.getShippingAddress())
                .status(OrderStatus.PENDING)
                .paymentStatus("UNPAID")
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu order và các order items (giả sử logic lưu items đã được xử lý)
        Orders savedOrder = orderRepository.save(order);
        return mapToOrderResponseDTO(savedOrder);
    }

    @Transactional
    public void cancelOrder(String orderId, String buyerId) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new RuntimeException("Order not found or not authorized"));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be canceled");
        }
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    public OrderHistoryResponse getOrderHistory(String buyerId) {
        List<Orders> orders = orderRepository.findByBuyerId(buyerId);
        List<OrderResponse> orderResponses = orders.stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
        return new OrderHistoryResponse(orderResponses);
    }

    public OrderResponse getOrderStatus(String orderId, String buyerId) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new RuntimeException("Order not found or not authorized"));
        return mapToOrderResponseDTO(order);
    }

    private OrderResponse mapToOrderResponseDTO(Orders order) {
        return OrderResponse.builder()
                .id(order.getId())
                .buyerId(order.getBuyer().getId())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .deliveryTrackingNumber(order.getDeliveryTrackingNumber())
                .createdAt(order.getCreatedAt())
                .build();
    }
}