package exe201.Refashion.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderImageUpdateRequest {
    private String orderId;
    private String status; // For paymentStatus or orderStatus
    private List<String> imageUrls;
    private String userId; // buyerId or sellerId
}