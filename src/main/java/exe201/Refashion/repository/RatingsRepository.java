package exe201.Refashion.repository;

import exe201.Refashion.entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingsRepository extends JpaRepository<Ratings, String> {
    boolean existsByReviewerIdAndProductId(String reviewerId, String productId);
}