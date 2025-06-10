package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.OrderResponse;
import exe201.Refashion.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "productIds", expression = "java(mapProductIds(order))")
    OrderResponse toOrderResponse(Orders order);

    @Named("mapProductIds")
    default List<String> mapProductIds(Orders order) {
        return order.getOrderItems() != null ? order.getOrderItems().stream()
                .map(item -> item.getProduct().getId())
                .collect(Collectors.toList()) : List.of();
    }
}