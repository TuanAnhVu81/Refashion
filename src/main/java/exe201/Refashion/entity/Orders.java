package exe201.Refashion.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Orders")
public class Orders {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    Users buyer;

    @Column(name = "total_amount", nullable = false)
    BigDecimal totalAmount;

    @Column(name = "shipping_address", nullable = false)
    String shippingAddress;

    @Column(name = "status")
    String status;

    @Column(name = "payment_status")
    String paymentStatus;

    @Column(name = "delivery_tracking_number")
    String deliveryTrackingNumber;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
