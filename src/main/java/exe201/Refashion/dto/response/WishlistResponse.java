package exe201.Refashion.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WishlistResponse {
    private String id;
    private String userId;
    private String productId;
    private LocalDateTime createdAt;
}