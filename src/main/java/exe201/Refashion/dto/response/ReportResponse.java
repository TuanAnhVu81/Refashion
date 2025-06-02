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
public class ReportResponse {
    String id;
    String reporterId;
    String reportedUserId;
    String reportedProductId;
    String reason;
    String description;
    String status;
    LocalDateTime createdAt;
}