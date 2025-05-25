package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.FeaturedPaymentResponse;
import exe201.Refashion.entity.FeaturedPayment;
import org.springframework.stereotype.Component;

@Component
public class FeaturedPaymentMapper {

    public FeaturedPaymentResponse toFeaturedPaymentResponse(FeaturedPayment payment) {
        FeaturedPaymentResponse response = new FeaturedPaymentResponse();
        response.setId(payment.getId());
        response.setProductId(payment.getProduct().getId());
        response.setSellerId(payment.getSeller().getId());
        response.setAmount(payment.getAmount());
        response.setDurationDays(payment.getDurationDays());
        response.setPaymentDate(payment.getPaymentDate());
        return response;
    }
}