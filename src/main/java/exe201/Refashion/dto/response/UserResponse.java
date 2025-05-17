package exe201.Refashion.dto.response;

import lombok.*;

import exe201.Refashion.entity.Role;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String id;
    String username;
    String email;
    Role role;
    String fullName;
    String phoneNumber;
    String address;
    String profilePicture;
    LocalDateTime createdAt;
    Boolean emailVerified;
    Boolean active;
    String verificationToken;
}
