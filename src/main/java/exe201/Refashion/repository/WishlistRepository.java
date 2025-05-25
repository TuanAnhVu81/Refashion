package exe201.Refashion.repository;

import exe201.Refashion.entity.Wishlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlists, String> {
    Optional<Wishlists> findByUserIdAndProductId(String userId, String productId);
    List<Wishlists> findByUserId(String userId);
}