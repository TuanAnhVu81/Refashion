package exe201.Refashion.service;

import exe201.Refashion.dto.request.MessageRequest;
import exe201.Refashion.dto.response.MessageResponse;
import exe201.Refashion.entity.Messages;
import exe201.Refashion.entity.Users;
import exe201.Refashion.repository.MessagesRepository;
import exe201.Refashion.repository.SubscriptionsRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    MessagesRepository messagesRepository;
    UserRepository userRepository;
    SubscriptionsRepository subscriptionsRepository;

    @Transactional
    public MessageResponse sendMessage(String senderId, MessageRequest request) {
        Users sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        Users receiver = userRepository.findById(request.getReceiverId()).orElseThrow(() -> new RuntimeException("Receiver not found"));

        long freeMessageCount = messagesRepository.countFreeMessagesBySenderIdAndReceiverId(senderId, request.getReceiverId());
        boolean hasPremium = subscriptionsRepository.existsByUserIdAndStatus(senderId, "ACTIVE");

        boolean isFree = !hasPremium && freeMessageCount < 7;

        if (!hasPremium && !isFree) {
            throw new RuntimeException("Đã hết số tin nhắn miễn phí (7 tin). Vui lòng nâng cấp gói Premium!");
        }

        Messages message = Messages.builder()
                .id(UUID.randomUUID().toString())
                .sender(sender)
                .receiver(receiver)
                .message(request.getMessage())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .isFree(isFree)
                .build();

        Messages savedMessage = messagesRepository.save(message);
        return mapToMessageResponse(savedMessage);
    }

    private MessageResponse mapToMessageResponse(Messages message) {
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .receiverId(message.getReceiver().getId())
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .isRead(message.getIsRead())
                .isFree(message.getIsFree())
                .build();
    }
}