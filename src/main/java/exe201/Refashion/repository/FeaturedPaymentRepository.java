package exe201.Refashion.repository;

import exe201.Refashion.entity.FeaturedPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturedPaymentRepository extends JpaRepository<FeaturedPayment, String> {
}