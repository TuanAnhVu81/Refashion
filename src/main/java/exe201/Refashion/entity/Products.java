package exe201.Refashion.entity;

import exe201.Refashion.enums.ProductCondition;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "product_condition")
    ProductCondition productCondition;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductImages> images;

    @Column(name = "is_featured", nullable = false)
    Boolean isFeatured;

    @Column(name = "featured_until") // Thời gian hết hạn nổi bật
    LocalDateTime featuredUntil;

    @Column(name = "is_sold")
    Boolean isSold;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
        if (isFeatured == null) {
            isFeatured = false;
        }
        if (isSold == null) {
            isSold = false;
        }
    }
}
