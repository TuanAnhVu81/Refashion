package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.CartItemResponse;
import exe201.Refashion.dto.response.CartResponse;
import exe201.Refashion.entity.Cart;
import exe201.Refashion.entity.CartItems;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    default CartItemResponse toCartItemResponse(CartItems item) {
        var product = item.getProduct();
        String imageUrl = product.getImages() != null && !product.getImages().isEmpty()
                ? product.getImages().get(0).getImageUrl()
                : null;

        return CartItemResponse.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .productImage(imageUrl)
                .price(product.getPrice())
                .build();
    }

    default List<CartItemResponse> toCartItemResponseList(List<CartItems> items) {
        return items.stream().map(this::toCartItemResponse).toList();
    }

    @Named("mapCartToCartResponse")
    default CartResponse toCartResponse(Cart cart, List<CartItems> items) {
        List<CartItemResponse> itemResponses = toCartItemResponseList(items);
        BigDecimal total = itemResponses.stream()
                .map(i -> i.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .items(itemResponses)
                .total(total)
                .build();
    }
}
