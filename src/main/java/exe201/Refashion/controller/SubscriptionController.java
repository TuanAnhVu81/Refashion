package exe201.Refashion.controller;

import exe201.Refashion.dto.request.SubscriptionRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.SubscriptionResponse;
import exe201.Refashion.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SubscriptionController {

    SubscriptionService subscriptionService;

    @PostMapping
    public ApiResponse<SubscriptionResponse> subscribe(@RequestParam String userId, @RequestBody SubscriptionRequest request) {
        SubscriptionResponse subscription = subscriptionService.subscribe(userId, request);
        return ApiResponse.<SubscriptionResponse>builder()
                .result(subscription)
                .build();
    }

    @DeleteMapping("/{subscriptionId}")
    public ApiResponse<Void> cancelSubscription(@PathVariable String subscriptionId, @RequestParam String userId) {
        subscriptionService.cancelSubscription(subscriptionId, userId);
        return ApiResponse.<Void>builder()
                .message("Hủy gói Premium thành công")
                .build();
    }
}