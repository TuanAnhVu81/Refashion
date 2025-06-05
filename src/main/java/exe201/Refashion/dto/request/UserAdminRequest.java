package exe201.Refashion.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAdminRequest {
    String action; // BAN, DELETE, UPGRADE
    String newRole; // Chỉ cần khi action là UPGRADE
}