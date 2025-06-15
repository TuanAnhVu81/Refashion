package exe201.Refashion.service;

import exe201.Refashion.dto.request.RatingRequest;
import exe201.Refashion.dto.response.RatingResponse;
import exe201.Refashion.dto.response.RatingSummaryResponse;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Ratings;
import exe201.Refashion.entity.Users;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.RatingsRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingService {

    RatingsRepository ratingsRepository;
    UserRepository userRepository;
    ProductRepository productRepository;

    //User
    @Transactional
    public RatingResponse rateProduct(String reviewerId, RatingRequest request) {
        Users reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new AppException(ErrorCode.INVALID_RATING);
        }

        if (ratingsRepository.existsByReviewerIdAndProductId(reviewerId, request.getProductId())) {
            throw new AppException(ErrorCode.RATING_ALREADY_EXISTS);
        }

        Ratings rating = Ratings.builder()
                .id(UUID.randomUUID().toString())
                .reviewer(reviewer)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        return mapToRatingResponse(ratingsRepository.save(rating));
    }

    @Transactional
    public RatingResponse updateRating(String ratingId, RatingRequest request) {
        Ratings rating = ratingsRepository.findById(ratingId)
                .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new AppException(ErrorCode.INVALID_RATING);
        }

        rating.setRating(request.getRating());
        rating.setComment(request.getComment());

        return mapToRatingResponse(ratingsRepository.save(rating));
    }

    @Transactional
    public void deleteRating(String ratingId) {
        Ratings rating = ratingsRepository.findById(ratingId)
                .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));
        ratingsRepository.delete(rating);
    }

    @Transactional(readOnly = true)
    public RatingResponse getRatingById(String ratingId) {
        Ratings rating = ratingsRepository.findById(ratingId)
                .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));
        return mapToRatingResponse(rating);
    }

    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByProductId(String productId) {
        return ratingsRepository.findByProductId(productId)
                .stream()
                .map(this::mapToRatingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RatingSummaryResponse getRatingSummary(String productId) {
        long totalVotes = ratingsRepository.countByProductId(productId);
        Double avgRating = ratingsRepository.calculateAverageRatingByProductId(productId);
        if (avgRating == null) {
            avgRating = 0.0;
        }

        return RatingSummaryResponse.builder()
                .productId(productId)
                .totalVotes(totalVotes)
                .averageRating(avgRating)
                .build();
    }

    private RatingResponse mapToRatingResponse(Ratings rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .reviewerId(rating.getReviewer().getId())
                .productId(rating.getProduct() != null ? rating.getProduct().getId() : null)
                .rating(rating.getRating())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .build();
    }
}
