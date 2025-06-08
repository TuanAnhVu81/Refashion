package exe201.Refashion.repository;

import exe201.Refashion.entity.Cart;
import exe201.Refashion.entity.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, String> {
    Optional<Cart> findByUser(Users user);
    boolean existsByUser(Users user);

}
