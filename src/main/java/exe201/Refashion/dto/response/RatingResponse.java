package exe201.Refashion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    String id;
    String reviewerId;
    String productId;
    Integer rating;
    String comment;
    LocalDateTime createdAt;
}