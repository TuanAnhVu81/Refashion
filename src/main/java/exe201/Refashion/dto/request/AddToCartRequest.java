package exe201.Refashion.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartRequest {
    String userId;
    String productId;
    int quantity;
}
