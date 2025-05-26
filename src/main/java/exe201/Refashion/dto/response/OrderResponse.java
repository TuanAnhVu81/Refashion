package exe201.Refashion.dto.response;

import exe201.Refashion.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    String id;
    String buyerId;
    BigDecimal totalAmount;
    String shippingAddress;
    OrderStatus status;
    String paymentStatus;
    String deliveryTrackingNumber;
    LocalDateTime createdAt;
}
