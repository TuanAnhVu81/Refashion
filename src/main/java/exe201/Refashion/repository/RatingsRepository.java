package exe201.Refashion.repository;

import exe201.Refashion.entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingsRepository extends JpaRepository<Ratings, String> {
    boolean existsByReviewerIdAndProductId(String reviewerId, String productId);

    long countByProductId(String productId);

    @Query("SELECT AVG(r.rating) FROM Ratings r WHERE r.product.id = :productId")
    Double calculateAverageRatingByProductId(@Param("productId") String productId);

    List<Ratings> findByProductId(String productId);
}