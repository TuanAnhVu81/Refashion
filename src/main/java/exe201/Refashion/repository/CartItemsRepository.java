package exe201.Refashion.repository;

import exe201.Refashion.entity.Cart;
import exe201.Refashion.entity.CartItems;
import exe201.Refashion.entity.Products;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends CrudRepository<CartItems, String> {
    Optional<CartItems> findByCartAndProduct(Cart cart, Products product);
    List<CartItems> findByCart(Cart cart);
    void deleteByCartAndProduct(Cart cart, Products product);

}
