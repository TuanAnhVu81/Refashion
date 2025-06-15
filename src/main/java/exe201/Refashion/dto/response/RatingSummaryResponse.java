package exe201.Refashion.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingSummaryResponse {
    String productId;
    long totalVotes;
    double averageRating;
}
