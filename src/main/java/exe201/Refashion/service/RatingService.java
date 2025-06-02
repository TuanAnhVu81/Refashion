package exe201.Refashion.service;

import exe201.Refashion.dto.request.RatingRequest;
import exe201.Refashion.dto.response.RatingResponse;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Ratings;
import exe201.Refashion.entity.Users;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.RatingsRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingService {

    RatingsRepository ratingsRepository;
    UserRepository userRepository;
    ProductRepository productRepository;

    @Transactional
    public RatingResponse rateProduct(String reviewerId, RatingRequest request) {
        Users reviewer = userRepository.findById(reviewerId).orElseThrow(() -> new RuntimeException("Reviewer not found"));
        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        if (ratingsRepository.existsByReviewerIdAndProductId(reviewerId, request.getProductId())) {
            throw new RuntimeException("Rating already exists");
        }

        Ratings rating = Ratings.builder()
                .id(UUID.randomUUID().toString())
                .reviewer(reviewer)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        Ratings savedRating = ratingsRepository.save(rating);
        return mapToRatingResponse(savedRating);
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