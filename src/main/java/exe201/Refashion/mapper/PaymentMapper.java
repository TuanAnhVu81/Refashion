package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.PaymentResponse;
import exe201.Refashion.entity.Payments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "order.id", target = "orderId")
    PaymentResponse toPaymentResponse(Payments p);
}