package exe201.Refashion.repository;

import exe201.Refashion.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {
    @Query("SELECT o FROM Orders o WHERE o.buyer.id = :buyerId")
    List<Orders> findByBuyerId(@Param("buyerId") String buyerId);
    List<Orders> findBySellerId(@Param("sellerId")String sellerId);
    Optional<Orders> findByIdAndBuyerId(String id, String buyerId);
    List<Orders> findByBuyerIdAndStatus(String buyerId, String status);

    @Query("SELECT DISTINCT o FROM Orders o JOIN o.orderItems oi WHERE oi.product.id = :productId")
    List<Orders> findByProductId(@Param("productId") String productId);

}
