package exe201.Refashion.dto.response;

import exe201.Refashion.enums.ProductCondition;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    String id;
    String title;
    String description;
    String brand;
    ProductCondition productCondition;
    String size;
    String color;
    BigDecimal price;
    String imageUrl;
    String categoryName;
    String sellerUsername;
    LocalDateTime createdAt;
    Boolean isActive;
}
