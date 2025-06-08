package exe201.Refashion.repository;

import exe201.Refashion.entity.Categories;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.enums.ProductStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Products, String> {
    List<Products> findByCategoryId(String categoryId);

    List<Products> findBySellerId(String sellerId);

    @Query("SELECT p FROM Products p WHERE (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY CASE WHEN (p.isFeatured = true AND (p.featuredUntil IS NULL OR p.featuredUntil > CURRENT_TIMESTAMP)) THEN 1 ELSE 0 END DESC, " +
            "CASE WHEN :sortBy = 'category.name' THEN p.category.name END ASC, " +
            "CASE WHEN :sortBy = 'category.name' AND :sortDirection = 'desc' THEN p.category.name END DESC, " +
            "CASE WHEN :sortBy = 'price' THEN p.price END ASC, " +
            "CASE WHEN :sortBy = 'price' AND :sortDirection = 'desc' THEN p.price END DESC, " +
            "CASE WHEN :sortBy = 'size' THEN p.size END ASC, " +
            "CASE WHEN :sortBy = 'size' AND :sortDirection = 'desc' THEN p.size END DESC, " +
            "CASE WHEN :sortBy = 'color' THEN p.color END ASC, " +
            "CASE WHEN :sortBy = 'color' AND :sortDirection = 'desc' THEN p.color END DESC, " +
            "CASE WHEN :sortBy = 'productCondition' THEN p.productCondition END ASC, " +
            "CASE WHEN :sortBy = 'productCondition' AND :sortDirection = 'desc' THEN p.productCondition END DESC, " +
            "p.title ASC")
    List<Products> findAllCustom(String keyword, String sortBy, String sortDirection);

    @Query("SELECT p FROM Products p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Products> searchProducts(String keyword);

    @EntityGraph(attributePaths = {"images"})
    Optional<Products> findWithImagesById(String id);

    List<Products> findByStatus(ProductStatus status);

}
