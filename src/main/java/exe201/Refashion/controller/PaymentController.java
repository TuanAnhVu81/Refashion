package exe201.Refashion.controller;

import exe201.Refashion.dto.request.PaymentRequest;
import exe201.Refashion.dto.response.PaymentResponse;
import exe201.Refashion.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(@RequestBody PaymentRequest req) {
        return paymentService.createPayment(req);
    }

    @PostMapping("/{id}/mark-shipped")
    public PaymentResponse markAsShipped(@PathVariable String id, @RequestParam String carrier, @RequestParam String tracking) {
        return paymentService.sellerMarksShipped(id, carrier, tracking);
    }

    @PostMapping("/{id}/mark-completed")
    public PaymentResponse markAsCompleted(@PathVariable String id) {
        return paymentService.buyerMarksCompleted(id);
    }
}