package exe201.Refashion.service;

import exe201.Refashion.dto.request.MessageRequest;
import exe201.Refashion.dto.response.MessageResponse;
import exe201.Refashion.dto.response.UserResponse;
import exe201.Refashion.entity.Messages;
import exe201.Refashion.entity.Users;
import exe201.Refashion.mapper.UserMapper;
import exe201.Refashion.repository.MessagesRepository;
import exe201.Refashion.repository.SubscriptionsRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    MessagesRepository messagesRepository;
    UserRepository userRepository;
    SubscriptionsRepository subscriptionsRepository;
    UserMapper userMapper;

    @Transactional
    public MessageResponse sendMessage(String senderId, String receiverId, MessageRequest request) {
        Users sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        Users receiver = userRepository.findById(request.getReceiverId()).orElseThrow(() -> new RuntimeException("Receiver not found"));

        long freeMessageCount = messagesRepository.countFreeMessagesBySenderIdAndReceiverId(senderId, request.getReceiverId());
        boolean hasPremium = subscriptionsRepository.existsByUserIdAndStatus(senderId, "ACTIVE");

        LocalDateTime todayStart = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        long todayCount = messagesRepository.countFreeMessagesSince(senderId, receiverId, todayStart);

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

    public List<MessageResponse> getConversation(String userId1, String userId2) {
        List<Messages> messages = messagesRepository
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAtAsc(userId1, userId2);
        return messages.stream().map(this::mapToMessageResponse).toList();
    }

    @Transactional
    public void markMessagesAsRead(String senderId, String receiverId) {
        messagesRepository.markAsRead(senderId, receiverId);
    }

    public List<UserResponse> getChatPartners(String userId) {
        List<Users> users = messagesRepository.findChatPartners(userId);
        return users.stream().map(userMapper::toUserResponse).toList(); // giả sử bạn có mapper
    }

}