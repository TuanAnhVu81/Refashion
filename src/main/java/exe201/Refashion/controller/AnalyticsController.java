package exe201.Refashion.controller;

import exe201.Refashion.dto.request.AnalyticsRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.AnalyticsResponse;
import exe201.Refashion.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/admin/analytics")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AnalyticsController {

    AnalyticsService analyticsService;

    @PostMapping("/users")
    public ApiResponse<AnalyticsResponse> getUserAnalytics(@RequestBody AnalyticsRequest request) {
        AnalyticsResponse response = analyticsService.getUserAnalytics(request);
        return ApiResponse.<AnalyticsResponse>builder()
                .result(response)
                .build();
    }
}