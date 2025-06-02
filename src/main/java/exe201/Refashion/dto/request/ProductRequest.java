package exe201.Refashion.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import exe201.Refashion.entity.Categories;
import exe201.Refashion.entity.Users;
import exe201.Refashion.enums.ProductCondition;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    String title;
    String description;
    String brand;
    ProductCondition productCondition;
    String size;
    String color;
    BigDecimal price;
    String categoryId;
    String sellerId;
    Boolean isFeatured;
    LocalDateTime featuredUntil; // Thêm trường mới
    Boolean isActive;

    @JsonIgnore
    MultipartFile imageFile; // Upload ảnh
}
