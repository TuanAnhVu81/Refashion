package exe201.Refashion.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    String id;
    String sellerId;
    String title;
    String description;
    String categoryId;
    String brand;
    String productCondition;
    String size;
    String color;
    BigDecimal price;
    List<String> imageUrls;
}
