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
@Table(name = "Payments")
public class Payments {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Orders order;

    @Column(name = "payment_method")
    String paymentMethod;

    @Column(name = "transaction_id")
    String transactionId;

    @Column(name = "amount")
    BigDecimal amount;

    @Column(name = "status")
    String status;

    @Column(name = "paid_at")
    LocalDateTime paidAt;
}
