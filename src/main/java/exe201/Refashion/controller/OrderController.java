package exe201.Refashion.controller;

import exe201.Refashion.dto.request.OrderRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.OrderHistoryResponse;
import exe201.Refashion.dto.response.OrderResponse;
import exe201.Refashion.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(orderRequest);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    @DeleteMapping("/{orderId}")
    public ApiResponse<Void> cancelOrder(@PathVariable String orderId, @RequestParam String buyerId) {
        orderService.cancelOrder(orderId, buyerId);
        return ApiResponse.<Void>builder()
                .message("Hủy đơn hàng thành công")
                .build();
    }

    @GetMapping("/buyer/{buyerId}")
    public ApiResponse<OrderHistoryResponse> getOrderHistory(@PathVariable String buyerId) {
        OrderHistoryResponse history = orderService.getOrderHistory(buyerId);
        return ApiResponse.<OrderHistoryResponse>builder()
                .result(history)
                .build();
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orders)
                .build();
    }

    @GetMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> getOrderStatus(@PathVariable String orderId, @RequestParam String buyerId) {
        OrderResponse order = orderService.getOrderStatus(orderId, buyerId);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }
    @PatchMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrderStatus(orderId, status))
                .build();
    }
    @GetMapping("/seller/{sellerId}")
    public ApiResponse<List<OrderResponse>> getOrdersBySeller(@PathVariable String sellerId) {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrdersBySeller(sellerId))
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable String orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    @PatchMapping("/{orderId}/payment-status")
    public ApiResponse<OrderResponse> updatePaymentStatus(
            @PathVariable String orderId,
            @RequestParam String paymentStatus,
            @RequestPart(required = false) MultipartFile paymentImage) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updatePaymentStatusWithImage(orderId, paymentStatus, paymentImage))
                .build();
    }

    @PatchMapping("/{orderId}/shipped")
    public ApiResponse<OrderResponse> updateToShipped(
            @PathVariable String orderId,
            @RequestPart MultipartFile packageImage,
            @RequestParam String sellerId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateToShipped(orderId, packageImage, sellerId))
                .build();
    }

    @PatchMapping("/{orderId}/delivered")
    public ApiResponse<OrderResponse> confirmDelivered(
            @PathVariable String orderId,
            @RequestPart MultipartFile packageImage,
            @RequestParam String buyerId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.confirmDelivered(orderId, packageImage, buyerId))
                .build();
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<List<OrderResponse>> getOrdersByProductId(@PathVariable String productId) {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrdersByProductId(productId))
                .build();
    }
}