package exe201.Refashion.dto.response;

import exe201.Refashion.enums.ProductCondition;
import exe201.Refashion.enums.ProductStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    String categoryName;
    String sellerUsername;
    Boolean isFeatured;
    LocalDateTime featuredUntil; // Thêm trường mới
    List<String> imageUrls; // ✅ Đây là trường đúng cho response
    LocalDateTime createdAt;
    Boolean isActive;
    ProductStatus status; // 🆕 Thêm trường status

}
