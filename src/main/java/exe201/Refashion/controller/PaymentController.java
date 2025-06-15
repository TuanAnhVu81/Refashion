package exe201.Refashion.controller;

import exe201.Refashion.dto.request.PaymentRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.PaymentResponse;
import exe201.Refashion.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;

    //User
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentRequest req) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.createPayment(req))
                .build();
    }
}
