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
    @JoinColumn(name = "target_user_id")
    Users targetUser;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Products product;

    @Column(name = "reason")
    String reason;

    @Column(name = "description")
    String description;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "status")
    String status;
}
