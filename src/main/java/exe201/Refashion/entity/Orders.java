package exe201.Refashion.entity;

import exe201.Refashion.enums.OrderStatus;
import exe201.Refashion.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    Users buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    Users seller;

    @Column(name = "total_amount", nullable = false)
    BigDecimal totalAmount;

    @Column(name = "shipping_address", nullable = false)
    String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    PaymentStatus paymentStatus;

    @Column(name = "delivery_tracking_number")
    String deliveryTrackingNumber;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItems> orderItems;

    @Column(name = "payment_screenshot_url")
    String paymentScreenshotUrl;

    @Column(name = "seller_package_image_url")
    String sellerPackageImageUrl;

    @Column(name = "buyer_package_image_url")
    String buyerPackageImageUrl;

    @Column(name = "admin_payment_screenshot_url")
    String adminPaymentScreenshotUrl;

}
