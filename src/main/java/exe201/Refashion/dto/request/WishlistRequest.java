package exe201.Refashion.dto.request;

import lombok.Data;

@Data
public class WishlistRequest {
    private String userId;
    private String productId;
}