package exe201.Refashion.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeatureProductRequest {
    private String productId;
    private String sellerId;
    private BigDecimal amount;
    private Integer durationDays; // Số ngày muốn nổi bật
}