package exe201.Refashion.service;

import exe201.Refashion.dto.request.UserAdminRequest;
import exe201.Refashion.dto.response.UserAdminResponse;
import exe201.Refashion.entity.Role;
import exe201.Refashion.entity.Users;
import exe201.Refashion.repository.RoleRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAdminService {

    UserRepository userRepository;
    RoleRepository roleRepository;

    @Transactional
    public UserAdminResponse manageUser(String userId, UserAdminRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getAction().equals("BAN")) {
            user.setActive(false);
        } else if (request.getAction().equals("DELETE")) {
            userRepository.delete(user);
            return UserAdminResponse.builder()
                    .userId(userId)
                    .status("DELETED")
                    .message("User deleted successfully")
                    .build();
        } else if (request.getAction().equals("UPGRADE")) {
            Role newRole = roleRepository.findByRoleName(request.getNewRole())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(newRole);
        } else {
            throw new RuntimeException("Invalid action");
        }

        userRepository.save(user);

        return UserAdminResponse.builder()
                .userId(userId)
                .status(user.getActive() ? "ACTIVE" : "BANNED")
                .role(user.getRole().getRoleName())
                .message("User updated successfully")
                .build();
    }
}