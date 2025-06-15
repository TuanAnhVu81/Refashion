package exe201.Refashion.controller;

import exe201.Refashion.dto.request.RatingRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.RatingResponse;
import exe201.Refashion.dto.response.RatingSummaryResponse;
import exe201.Refashion.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RatingController {

    RatingService ratingService;

    @PostMapping
    public ApiResponse<RatingResponse> rateProduct(@RequestParam String reviewerId, @RequestBody RatingRequest request) {
        RatingResponse rating = ratingService.rateProduct(reviewerId, request);
        return ApiResponse.<RatingResponse>builder().result(rating).build();
    }

    @PutMapping("/{ratingId}")
    public ApiResponse<RatingResponse> updateRating(@PathVariable String ratingId, @RequestBody RatingRequest request) {
        RatingResponse updated = ratingService.updateRating(ratingId, request);
        return ApiResponse.<RatingResponse>builder().result(updated).build();
    }

    @DeleteMapping("/{ratingId}")
    public ApiResponse<Void> deleteRating(@PathVariable String ratingId) {
        ratingService.deleteRating(ratingId);
        return ApiResponse.<Void>builder().result(null).build();
    }

    @GetMapping("/{ratingId}")
    public ApiResponse<RatingResponse> getRatingById(@PathVariable String ratingId) {
        RatingResponse rating = ratingService.getRatingById(ratingId);
        return ApiResponse.<RatingResponse>builder().result(rating).build();
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<List<RatingResponse>> getRatingsByProduct(@PathVariable String productId) {
        List<RatingResponse> ratings = ratingService.getRatingsByProductId(productId);
        return ApiResponse.<List<RatingResponse>>builder().result(ratings).build();
    }

    @GetMapping("/summary/{productId}")
    public ApiResponse<RatingSummaryResponse> getRatingSummary(@PathVariable String productId) {
        RatingSummaryResponse summary = ratingService.getRatingSummary(productId);
        return ApiResponse.<RatingSummaryResponse>builder().result(summary).build();
    }
}
