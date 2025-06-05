package exe201.Refashion.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAdminResponse {
    String productId;
    String status;
    String message;
}