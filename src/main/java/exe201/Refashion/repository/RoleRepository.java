package exe201.Refashion.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import exe201.Refashion.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(String roleName);
    List<Role> findAll();
}
