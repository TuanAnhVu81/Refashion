package exe201.Refashion.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogCommentResponse {
    private String id;
    private String content;
    private String username;
    private String productId;
    private LocalDateTime createdAt;
}
