package exe201.Refashion.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogCommentRequest {
    private String blogId;
    private String userId;
    private String content;
}
