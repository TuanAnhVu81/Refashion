package exe201.Refashion.controller;

import exe201.Refashion.dto.request.MessageRequest;
import exe201.Refashion.dto.response.MessageResponse;
import exe201.Refashion.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.send") // client gửi tới /app/chat.send
    public void send(MessageRequest request) {
        MessageResponse saved = messageService.sendMessage(request.getSenderId(), request.getReceiverId(), request);
        // Gửi tới người nhận: /topic/messages.{receiverId}
        messagingTemplate.convertAndSend("/topic/messages." + request.getReceiverId(), saved);
    }
}
