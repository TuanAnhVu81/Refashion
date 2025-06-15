package exe201.Refashion.controller;

import exe201.Refashion.dto.request.FeatureProductRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.FeaturedPaymentResponse;
import exe201.Refashion.service.FeaturedPaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/featured-payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeaturedPaymentController {

    FeaturedPaymentService featuredPaymentService;

    // User: Request to feature a product
    @PostMapping
    public ApiResponse<FeaturedPaymentResponse> requestFeatureProduct(@RequestBody FeatureProductRequest request) {
        FeaturedPaymentResponse response = featuredPaymentService.requestFeatureProduct(request);
        return ApiResponse.<FeaturedPaymentResponse>builder()
                .result(response)
                .message("Yêu cầu nổi bật sản phẩm đã được gửi, chờ admin xác nhận")
                .build();
    }

    // Admin: Confirm or reject feature payment
    @PatchMapping("/{paymentId}/confirm")
    public ApiResponse<FeaturedPaymentResponse> confirmFeaturePayment(
            @PathVariable String paymentId,
            @RequestParam String adminId,
            @RequestParam boolean isConfirmed) {
        FeaturedPaymentResponse response = featuredPaymentService.confirmFeaturePayment(paymentId, adminId, isConfirmed);
        return ApiResponse.<FeaturedPaymentResponse>builder()
                .result(response)
                .message(isConfirmed ? "Xác nhận thanh toán thành công" : "Thanh toán đã bị từ chối")
                .build();
    }

    // Admin: Get all featured payments
    @GetMapping
    public ApiResponse<List<FeaturedPaymentResponse>> getAllFeaturedPayments(@RequestParam String adminId) {
        List<FeaturedPaymentResponse> responses = featuredPaymentService.getAllFeaturedPayments(adminId);
        return ApiResponse.<List<FeaturedPaymentResponse>>builder()
                .result(responses)
                .build();
    }

    // Admin: Get featured payment by ID
    @GetMapping("/{paymentId}")
    public ApiResponse<FeaturedPaymentResponse> getFeaturedPaymentById(
            @PathVariable String paymentId,
            @RequestParam String adminId) {
        FeaturedPaymentResponse response = featuredPaymentService.getFeaturedPaymentById(paymentId, adminId);
        return ApiResponse.<FeaturedPaymentResponse>builder()
                .result(response)
                .build();
    }

    // Admin: Update featured payment
    @PutMapping("/{paymentId}")
    public ApiResponse<FeaturedPaymentResponse> updateFeaturedPayment(
            @PathVariable String paymentId,
            @RequestBody FeatureProductRequest request,
            @RequestParam String adminId) {
        FeaturedPaymentResponse response = featuredPaymentService.updateFeaturedPayment(paymentId, request, adminId);
        return ApiResponse.<FeaturedPaymentResponse>builder()
                .result(response)
                .message("Cập nhật thanh toán nổi bật thành công")
                .build();
    }

    // Admin: Delete featured payment
    @DeleteMapping("/{paymentId}")
    public ApiResponse<Void> deleteFeaturedPayment(
            @PathVariable String paymentId,
            @RequestParam String adminId) {
        featuredPaymentService.deleteFeaturedPayment(paymentId, adminId);
        return ApiResponse.<Void>builder()
                .message("Xóa thanh toán nổi bật thành công")
                .build();
    }

    // Admin: Get pending featured payments
    @GetMapping("/pending")
    public ApiResponse<List<FeaturedPaymentResponse>> getPendingFeaturedPayments(@RequestParam String adminId) {
        List<FeaturedPaymentResponse> responses = featuredPaymentService.getPendingFeaturedPayments(adminId);
        return ApiResponse.<List<FeaturedPaymentResponse>>builder()
                .result(responses)
                .build();
    }
}