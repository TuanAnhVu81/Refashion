package exe201.Refashion.dto.response;

import exe201.Refashion.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {

    String token;
    boolean authenticated;
    String userId;
    Role role;
}
