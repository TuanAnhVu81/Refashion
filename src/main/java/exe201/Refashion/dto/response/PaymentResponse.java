package exe201.Refashion.dto.response;

import exe201.Refashion.enums.TransactionStatus;
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
    TransactionStatus escrowStatus;
    String shippingCarrier;
    String trackingNumber;
    LocalDateTime paidAt;
    LocalDateTime deliveredAt;
    LocalDateTime completedAt;
}