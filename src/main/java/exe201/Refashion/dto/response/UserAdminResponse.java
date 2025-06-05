package exe201.Refashion.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAdminResponse {
    String userId;
    String status;
    String role;
    String message;
}