package exe201.Refashion.repository;

import exe201.Refashion.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {
    List<ProductImages> findByProductId(String productId);
}
