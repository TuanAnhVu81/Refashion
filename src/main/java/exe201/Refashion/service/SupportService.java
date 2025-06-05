package exe201.Refashion.service;

import exe201.Refashion.dto.request.SupportRequest;
import exe201.Refashion.dto.response.SupportResponse;
import exe201.Refashion.entity.Support;
import exe201.Refashion.entity.Users;
import exe201.Refashion.repository.SupportRequestRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupportService {

    SupportRequestRepository supportRequestRepository;
    UserRepository userRepository;

    @Transactional
    public SupportResponse submitSupportRequest(String userId, SupportRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Support supportRequest = Support.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .subject(request.getSubject())
                .message(request.getMessage())
                .build();

        Support savedRequest = supportRequestRepository.save(supportRequest);

        return SupportResponse.builder()
                .id(savedRequest.getId())
                .userId(userId)
                .subject(savedRequest.getSubject())
                .message(savedRequest.getMessage())
                .status(savedRequest.getStatus())
                .createdAt(savedRequest.getCreatedAt())
                .build();
    }
}