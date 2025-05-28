package exe201.Refashion.dto.request;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    String orderId;
    String paymentMethod;
    String transactionId;
    BigDecimal amount;
    String shippingCarrier;
    String trackingNumber;
}