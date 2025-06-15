package exe201.Refashion.dto.response;

import exe201.Refashion.enums.OrderImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderImageResponse {
    String imageUrl;
    OrderImageType imageType; // PAYMENT, SELLER_PACKAGE, BUYER_PACKAGE
}
