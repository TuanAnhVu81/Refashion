package exe201.Refashion.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsRequest {
    LocalDateTime startDate;
    LocalDateTime endDate;
}