package exe201.Refashion.dto.response;

import exe201.Refashion.enums.OrderStatus;
import exe201.Refashion.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    String id;
    String buyerId;
    String sellerId;
    BigDecimal totalAmount;
    String shippingAddress;
    OrderStatus status;
    PaymentStatus paymentStatus;
    String deliveryTrackingNumber;
    LocalDateTime createdAt;
    List<String> productIds;
}
