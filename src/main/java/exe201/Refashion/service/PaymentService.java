package exe201.Refashion.service;

import exe201.Refashion.dto.request.PaymentRequest;
import exe201.Refashion.dto.response.PaymentResponse;
import exe201.Refashion.entity.Orders;
import exe201.Refashion.entity.Payments;
import exe201.Refashion.enums.PaymentStatus;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.PaymentMapper;
import exe201.Refashion.repository.OrderRepository;
import exe201.Refashion.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public PaymentResponse createPayment(PaymentRequest req) {
        Orders order = orderRepo.findById(req.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getPaymentStatus() != PaymentStatus.UNPAID) {
            throw new AppException(ErrorCode.INVALID_PAYMENT_STATUS_TRANSITION);
        }

        Payments payment = Payments.builder()
                .id(UUID.randomUUID().toString())
                .order(order)
                .amount(req.getAmount())
                .paymentMethod(req.getPaymentMethod())
                .transactionId(req.getTransactionId())
                .status("succeeded")
                .paymentStatus(PaymentStatus.UNPAID) // Chờ admin duyệt
                .paidAt(LocalDateTime.now())
                .build();

        return mapper.toPaymentResponse(paymentRepo.save(payment));
    }
}
