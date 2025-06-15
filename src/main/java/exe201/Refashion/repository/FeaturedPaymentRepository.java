package exe201.Refashion.repository;

import exe201.Refashion.entity.FeaturedPayment;
import exe201.Refashion.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeaturedPaymentRepository extends JpaRepository<FeaturedPayment, String> {
    List<FeaturedPayment> findByStatus(PaymentStatus status);
}