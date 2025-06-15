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

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    //User
    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(orderRequest);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    //User
    @DeleteMapping("/{orderId}")
    public ApiResponse<Void> cancelOrder(@PathVariable String orderId, @RequestParam String buyerId) {
        orderService.cancelOrder(orderId, buyerId);
        return ApiResponse.<Void>builder()
                .message("Hủy đơn hàng thành công")
                .build();
    }

    //User
    @PutMapping("/payment-image/{orderId}")
    public ApiResponse<OrderResponse> updatePaymentImage(
            @PathVariable String orderId,
            @RequestParam String buyerId,
            @RequestBody List<String> paymentImageUrls) {
        OrderResponse order = orderService.updatePaymentImage(orderId, buyerId, paymentImageUrls);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    //User
    @GetMapping("/buyer/{buyerId}")
    public ApiResponse<OrderHistoryResponse> getOrderHistory(@PathVariable String buyerId) {
        OrderHistoryResponse history = orderService.getOrderHistory(buyerId);
        return ApiResponse.<OrderHistoryResponse>builder()
                .result(history)
                .build();
    }

    //User, Admin
    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orders)
                .build();
    }

    //User, Admin
    @GetMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> getOrderStatus(@PathVariable String orderId, @RequestParam String buyerId) {
        OrderResponse order = orderService.getOrderStatus(orderId, buyerId);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    //Admin
    @PatchMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        OrderResponse order = orderService.updateOrderStatus(orderId, status);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    //User
    @GetMapping("/seller/{sellerId}")
    public ApiResponse<List<OrderResponse>> getOrdersBySeller(@PathVariable String sellerId) {
        List<OrderResponse> orders = orderService.getOrdersBySeller(sellerId);
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orders)
                .build();
    }

    //Admin
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable String orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    //Admin
    @PatchMapping("/{orderId}/payment-status")
    public ApiResponse<OrderResponse> updatePaymentStatus(
            @PathVariable String orderId,
            @RequestParam String paymentStatus,
            @RequestParam(required = false) List<String> imageUrls) {
        OrderResponse order = orderService.updatePaymentStatus(orderId, paymentStatus);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    //User (Seller)
    @PatchMapping("/{orderId}/shipped")
    public ApiResponse<OrderResponse> updateToShipped(
            @PathVariable String orderId,
            @RequestParam(required = false) List<String> imageUrls,
            @RequestParam String sellerId) {
        OrderResponse order = orderService.updateToShipped(orderId, imageUrls, sellerId);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    //User (Buyer): xác thực đã nhận hàng
    @PatchMapping("/{orderId}/delivered")
    public ApiResponse<OrderResponse> confirmDelivered(
            @PathVariable String orderId,
            @RequestParam(required = false) List<String> imageUrls,
            @RequestParam String buyerId) {
        OrderResponse order = orderService.confirmDelivered(orderId, imageUrls, buyerId);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    //User, Admin
    @GetMapping("/product/{productId}")
    public ApiResponse<List<OrderResponse>> getOrdersByProductId(@PathVariable String productId) {
        List<OrderResponse> orders = orderService.getOrdersByProductId(productId);
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orders)
                .build();
    }

    // Admin upload ảnh đã chuyển khoản cho người bán sau khi người mua đã nhận hàng
    @PatchMapping("/{orderId}/admin-payment-image")
    public ApiResponse<OrderResponse> uploadAdminPaymentImage(
            @PathVariable String orderId,
            @RequestParam List<String> imageUrls) {
        OrderResponse order = orderService.uploadAdminPaymentImage(orderId, imageUrls);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }

    // Admin chọn trạng thái hoàn hàng (REFUND)
    @PatchMapping("/{orderId}/refund")
    public ApiResponse<OrderResponse> markOrderAsRefund(
            @PathVariable String orderId) {
        OrderResponse order = orderService.markOrderAsRefund(orderId);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .message("Đơn hàng đã được đánh dấu hoàn hàng")
                .build();
    }


}