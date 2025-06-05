package exe201.Refashion.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {
    String id;
    String title;
    String content;
    String authorUsername;
    List<ProductResponse> products;
    LocalDateTime createdAt;
}
