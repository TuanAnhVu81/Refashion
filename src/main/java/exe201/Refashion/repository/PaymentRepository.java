package exe201.Refashion.repository;

import exe201.Refashion.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payments, String> {
}
