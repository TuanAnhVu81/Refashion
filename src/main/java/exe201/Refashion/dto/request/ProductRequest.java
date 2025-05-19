package exe201.Refashion.dto.request;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    String sellerId;
    String title;
    String description;
    String categoryId;
    String brand;
    String productCondition;
    String size;
    String color;
    String price;
    List<String> imageUrls;  // Thêm trường này

}
