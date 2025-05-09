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
@Table(name = "Products")
public class Products {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    Users seller;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Categories category;

    @Column(name = "brand")
    String brand;

    @Column(name = "product_condition")
    String productCondition;

    @Column(name = "size")
    String size;

    @Column(name = "color")
    String color;

    @Column(name = "price", nullable = false)
    BigDecimal price;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "is_active")
    Boolean isActive;
}
