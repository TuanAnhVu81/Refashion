package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.OrderResponse;
import exe201.Refashion.entity.OrderItems;
import exe201.Refashion.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "deliveryTrackingNumber", target = "deliveryTrackingNumber")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "orderItems", target = "productIds", qualifiedByName = "mapProductIds")
    @Mapping(source = "paymentScreenshotUrl", target = "paymentScreenshotUrl")
    @Mapping(source = "sellerPackageImageUrl", target = "sellerPackageImageUrl")
    @Mapping(source = "buyerPackageImageUrl", target = "buyerPackageImageUrl")
    OrderResponse toOrderResponse(Orders order);

    @Named("mapProductIds")
    default List<String> mapProductIds(List<OrderItems> orderItems) {
        return orderItems != null
                ? orderItems.stream()
                .map(item -> item.getProduct().getId())
                .collect(Collectors.toList())
                : List.of();
    }
}
