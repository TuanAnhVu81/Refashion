package exe201.Refashion.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    String username;
    String password;
    String confirmPassword; // <-- Thêm dòng này
    String fullName;
    String phoneNumber;
    String address;
    String profilePicture;
}