package exe201.Refashion.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeatureProductRequest {
    String productId;
    String sellerId;
    BigDecimal amount;
    Integer durationDays; // Số ngày muốn nổi bật
    String transferProofImageUrl;

}