package exe201.Refashion.service;

import exe201.Refashion.dto.request.PaymentRequest;
import exe201.Refashion.dto.response.PaymentResponse;
import exe201.Refashion.entity.Orders;
import exe201.Refashion.entity.Payments;
import exe201.Refashion.enums.PaymentStatus;
import exe201.Refashion.enums.TransactionStatus;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.PaymentMapper;
import exe201.Refashion.repository.OrderRepository;
import exe201.Refashion.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PaymentService {
    PaymentRepository paymentRepo;
    OrderRepository orderRepo;
    PaymentMapper mapper;

    public PaymentResponse createPayment(PaymentRequest req) {
        Orders order = orderRepo.findById(req.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        Payments p = Payments.builder()
                .id(UUID.randomUUID().toString())
                .order(order)
                .amount(req.getAmount())
                .paymentMethod(req.getPaymentMethod())
                .transactionId(req.getTransactionId())
                .status("succeeded")
                .escrowStatus(TransactionStatus.PAID)
                .paidAt(LocalDateTime.now())
                .build();
        order.setPaymentStatus(PaymentStatus.PAID); // cập nhật status
        orderRepo.save(order);

        return mapper.toPaymentResponse(paymentRepo.save(p));
    }

    public PaymentResponse sellerMarksShipped(String paymentId, String carrier, String tracking) {
        Payments p = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        p.setShippingCarrier(carrier);
        p.setTrackingNumber(tracking);
        p.setDeliveredAt(LocalDateTime.now());
        p.setEscrowStatus(TransactionStatus.DELIVERED);
        return mapper.toPaymentResponse(paymentRepo.save(p));
    }

    public PaymentResponse buyerMarksCompleted(String paymentId) {
        Payments p = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        p.setCompletedAt(LocalDateTime.now());
        p.setEscrowStatus(TransactionStatus.COMPLETED);
        return mapper.toPaymentResponse(paymentRepo.save(p));
    }
}