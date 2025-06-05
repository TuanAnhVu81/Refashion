package exe201.Refashion.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponse {
    long totalUsers;
    BigDecimal totalRevenue;
    double averageRating;
    LocalDateTime startDate;
    LocalDateTime endDate;
}