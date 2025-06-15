package exe201.Refashion.dto.response;

import exe201.Refashion.enums.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    String id;
    String orderId;
    String paymentMethod;
    String transactionId;
    BigDecimal amount;
    String status;
    PaymentStatus paymentStatus;
    String shippingCarrier;
    String trackingNumber;
    LocalDateTime paidAt;
    LocalDateTime deliveredAt;
    LocalDateTime completedAt;
}