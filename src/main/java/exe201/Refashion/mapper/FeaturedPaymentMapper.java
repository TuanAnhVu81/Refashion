package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.FeaturedPaymentResponse;
import exe201.Refashion.entity.FeaturedPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeaturedPaymentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "durationDays", target = "durationDays")
    @Mapping(source = "paymentDate", target = "paymentDate")
    @Mapping(source = "transferProofImageUrl", target = "transferProofImageUrl")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    FeaturedPaymentResponse toFeaturedPaymentResponse(FeaturedPayment payment);

    @org.mapstruct.Named("mapStatus")
    default String mapStatus(Enum<?> status) {
        return status != null ? status.name() : null;
    }
}
