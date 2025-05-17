package exe201.Refashion.repository;

import exe201.Refashion.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);
    Optional<Users> findByVerificationToken(String verificationToken);
}
