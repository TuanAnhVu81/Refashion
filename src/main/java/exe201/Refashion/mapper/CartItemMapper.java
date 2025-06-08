package exe201.Refashion.mapper;

import exe201.Refashion.dto.request.AddToCartRequest;
import exe201.Refashion.entity.CartItems;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItems fromAddToCartRequest(AddToCartRequest request);
}
