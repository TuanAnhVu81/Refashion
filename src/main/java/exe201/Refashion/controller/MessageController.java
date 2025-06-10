package exe201.Refashion.controller;

import exe201.Refashion.dto.request.MessageRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.MessageResponse;
import exe201.Refashion.dto.response.UserResponse;
import exe201.Refashion.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MessageController {

    MessageService messageService;

    @PostMapping("/send")
    public ApiResponse<MessageResponse> sendMessage(@RequestParam String senderId, @RequestParam String receiverId, @RequestBody MessageRequest request) {
        MessageResponse message = messageService.sendMessage(senderId, receiverId, request);
        return ApiResponse.<MessageResponse>builder()
                .result(message)
                .build();
    }

    @GetMapping("/conversation")
    public ApiResponse<List<MessageResponse>> getConversation(@RequestParam String userId1, @RequestParam String userId2) {
        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.getConversation(userId1, userId2))
                .build();
    }

    @PostMapping("/read")
    public ApiResponse<Void> markAsRead(@RequestParam String senderId, @RequestParam String receiverId) {
        messageService.markMessagesAsRead(senderId, receiverId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/partners")
    public ApiResponse<List<UserResponse>> getChatPartners(@RequestParam String userId) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(messageService.getChatPartners(userId))
                .build();
    }

}