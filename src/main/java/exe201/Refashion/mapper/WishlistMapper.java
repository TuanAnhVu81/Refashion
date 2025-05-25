package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.WishlistResponse;
import exe201.Refashion.entity.Wishlists;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistResponse toWishlistResponse(Wishlists wishlist) {
        WishlistResponse response = new WishlistResponse();
        response.setId(wishlist.getId());
        response.setUserId(wishlist.getUser().getId());
        response.setProductId(wishlist.getProduct().getId());
        response.setCreatedAt(wishlist.getCreatedAt());
        return response;
    }
}