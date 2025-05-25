package exe201.Refashion.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    String id;
    String name;
    String description;
}
