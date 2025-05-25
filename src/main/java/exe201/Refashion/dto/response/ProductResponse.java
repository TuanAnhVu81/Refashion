package exe201.Refashion.dto.response;

import exe201.Refashion.enums.ProductCondition;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    String id;
    String title;
    String description;
    String brand;
    ProductCondition productCondition;
    String size;
    String color;
    BigDecimal price;
    String imageUrl;
    String categoryName;
    String sellerUsername;
    Boolean isFeatured;
    LocalDateTime featuredUntil; // Thêm trường mới
    MultipartFile imageFile; // Upload ảnh
    LocalDateTime createdAt;
    Boolean isActive;
}
