package exe201.Refashion.repository;

import exe201.Refashion.entity.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions, String> {
    boolean existsByUserIdAndStatus(String userId, String status);
}