package exe201.Refashion.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequest {
    String title;
    String content;
    String authorId; // userId
    List<String> productIds; // danh sách sản phẩm đã duyệt
}
