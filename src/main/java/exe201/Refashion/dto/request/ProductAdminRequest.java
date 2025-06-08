package exe201.Refashion.dto.request;

import exe201.Refashion.enums.ProductStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAdminRequest {
    ProductStatus status; // Chỉ chấp nhận APPROVED hoặc REJECTED
    String note; // Lý do từ chối nếu cần
}