package exe201.Refashion.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FeaturedPaymentResponse {
    private String id;
    private String productId;
    private String sellerId;
    private BigDecimal amount;
    private Integer durationDays;
    private LocalDateTime paymentDate;
}