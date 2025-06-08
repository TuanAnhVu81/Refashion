package exe201.Refashion.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    String productId;
    String title;
    String productImage;
    BigDecimal price;

}
