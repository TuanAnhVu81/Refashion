package exe201.Refashion.controller;

import exe201.Refashion.dto.request.MessageRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.MessageResponse;
import exe201.Refashion.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MessageController {

    MessageService messageService;

    @PostMapping("/send")
    public ApiResponse<MessageResponse> sendMessage(@RequestParam String senderId, @RequestBody MessageRequest request) {
        MessageResponse message = messageService.sendMessage(senderId, request);
        return ApiResponse.<MessageResponse>builder()
                .result(message)
                .build();
    }
}