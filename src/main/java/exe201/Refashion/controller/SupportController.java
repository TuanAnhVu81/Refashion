package exe201.Refashion.controller;

import exe201.Refashion.dto.request.SupportRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.SupportResponse;
import exe201.Refashion.service.SupportService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SupportController {

    SupportService supportService;

    @PostMapping
    public ApiResponse<SupportResponse> submitSupportRequest(
            @RequestParam String userId,
            @RequestBody SupportRequest request) {
        SupportResponse response = supportService.submitSupportRequest(userId, request);
        return ApiResponse.<SupportResponse>builder()
                .result(response)
                .build();
    }
}