package exe201.Refashion.repository;

import exe201.Refashion.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, String> {
    // Thêm query nếu cần
    boolean existsByName(String name);

}
