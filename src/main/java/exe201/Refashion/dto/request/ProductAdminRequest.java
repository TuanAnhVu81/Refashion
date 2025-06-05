package exe201.Refashion.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAdminRequest {
    String action; // APPROVE, REJECT, DELETE
}