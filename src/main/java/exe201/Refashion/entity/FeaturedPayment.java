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
@Table(name = "FeaturedPayments")
public class FeaturedPayment { //Cho việc trả phí để đưa món hàng lên đầu trang khi người dùng tìm kiếm
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    Users seller;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Products product;

    @Column(name = "amount", nullable = false)
    BigDecimal amount;

    @Column(name = "payment_date")
    LocalDateTime paymentDate;

    @Column(name = "duration_days")
    Integer durationDays; // Số ngày nổi bật

    @PrePersist
    public void prePersist() {
        paymentDate = LocalDateTime.now();
    }
}