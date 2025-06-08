package exe201.Refashion.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    String id;
    String userId;
    List<CartItemResponse> items;
    BigDecimal total;
}
