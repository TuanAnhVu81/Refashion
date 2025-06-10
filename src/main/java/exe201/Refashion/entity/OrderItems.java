package exe201.Refashion.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "OrderItems")
public class OrderItems {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Orders order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Products product;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "price_at_purchase", nullable = false)
    BigDecimal priceAtPurchase;
}
