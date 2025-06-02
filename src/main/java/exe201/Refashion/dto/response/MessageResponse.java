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
public class MessageResponse {
    String id;
    String senderId;
    String receiverId;
    String message;
    LocalDateTime sentAt;
    Boolean isRead;
    Boolean isFree;
}