package exe201.Refashion.controller;

import exe201.Refashion.dto.request.RatingRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.RatingResponse;
import exe201.Refashion.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RatingController {

    RatingService ratingService;

    @PostMapping
    public ApiResponse<RatingResponse> rateProduct(@RequestParam String reviewerId, @RequestBody RatingRequest request) {
        System.out.println("ReviewerId: " + reviewerId);
        System.out.println("Request: " + request);
        RatingResponse rating = ratingService.rateProduct(reviewerId, request);
        return ApiResponse.<RatingResponse>builder()
                .result(rating)
                .build();
    }
}