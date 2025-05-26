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

    @GetMapping("/history")
    public ApiResponse<OrderHistoryResponse> getOrderHistory(@RequestParam String buyerId) {
        OrderHistoryResponse history = orderService.getOrderHistory(buyerId);
        return ApiResponse.<OrderHistoryResponse>builder()
                .result(history)
                .build();
    }

    @GetMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> getOrderStatus(@PathVariable String orderId, @RequestParam String buyerId) {
        OrderResponse order = orderService.getOrderStatus(orderId, buyerId);
        return ApiResponse.<OrderResponse>builder()
                .result(order)
                .build();
    }
}