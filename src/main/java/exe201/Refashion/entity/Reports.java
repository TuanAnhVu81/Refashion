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
@Table(name = "Reports")
public class Reports {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    Users reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    Users reportedUser;

    @ManyToOne
    @JoinColumn(name = "reported_product_id")
    Products reportedProduct;

    @Column(name = "reason")
    String reason;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    String status;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}