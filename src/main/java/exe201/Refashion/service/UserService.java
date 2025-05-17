package exe201.Refashion.service;

import exe201.Refashion.dto.request.UserCreationRequest;
import exe201.Refashion.dto.response.UserResponse;
import exe201.Refashion.entity.Role;
import exe201.Refashion.entity.Users;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.UserMapper;
import exe201.Refashion.repository.RoleRepository;
import exe201.Refashion.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXIST);
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXIST);

        Users user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRole(role);

        Users savedUser = userRepository.save(user);
        // Tạo token xác thực
        String verificationToken = java.util.UUID.randomUUID().toString();
        savedUser.setVerificationToken(verificationToken);

        // Lưu lại user với token
        savedUser = userRepository.save(savedUser); // Lưu lại để cập nhật token

        // Gửi email xác thực
        try {
            log.info("Attempting to send verification email to: {}", savedUser.getEmail());
            emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);
        }catch(MessagingException e){
                log.error("Failed to send verification email", e);
                throw new RuntimeException(e);
            }
            return userMapper.toUserResponse(savedUser);
    }

    public void verifyEmail(String token) {
        Users user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_VERIFICATION_TOKEN));

        if (user.getEmailVerified()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null); // Xóa token sau khi xác thực
        userRepository.save(user);
    }

}
