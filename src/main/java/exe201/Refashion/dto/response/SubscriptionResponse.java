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
public class SubscriptionResponse {
    String id;
    String userId;
    String packageType;
    Integer messageLimit;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String status;
}