package exe201.Refashion.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportResponse {
    String id;
    String userId;
    String subject;
    String message;
    String status;
    LocalDateTime createdAt;
}