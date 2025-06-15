package exe201.Refashion.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeaturedPaymentResponse {
    String id;
    String productId;
    String sellerId;
    BigDecimal amount;
    Integer durationDays;
    LocalDateTime paymentDate;
    String transferProofImageUrl;
    String status;
}