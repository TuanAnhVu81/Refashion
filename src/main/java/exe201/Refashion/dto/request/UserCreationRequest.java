package exe201.Refashion.dto.request;

import jakarta.persistence.PrePersist;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    String username;
    String email;
    String password;
    String confirmPassword; // <-- Thêm dòng này
    String fullName;
    String phoneNumber;
    String address;
    String roleId;
    Boolean active;
    @PrePersist
    protected void onCreate() {
        if (active == null) {
            active = true;
        }
    }
}