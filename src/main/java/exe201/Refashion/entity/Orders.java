package exe201.Refashion.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @ManyToOne
    @JoinColumn(name = "seller_id")
    Users seller;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Products product;

    @Column(name = "status")
    String status;

    @Column(name = "payment_status")
    String paymentStatus;

    @Column(name = "delivery_tracking_number")
    String deliveryTrackingNumber;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
