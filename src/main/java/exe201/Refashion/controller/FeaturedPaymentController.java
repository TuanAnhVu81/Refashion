package exe201.Refashion.controller;

import exe201.Refashion.dto.request.FeatureProductRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.FeaturedPaymentResponse;
import exe201.Refashion.service.FeaturedPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/featured-payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeaturedPaymentController {

    FeaturedPaymentService featuredPaymentService;

    @PostMapping
    public ApiResponse<FeaturedPaymentResponse> featureProduct(@RequestBody FeatureProductRequest request) {
        return ApiResponse.<FeaturedPaymentResponse>builder()
                .result(featuredPaymentService.featureProduct(request))
                .build();
    }
}