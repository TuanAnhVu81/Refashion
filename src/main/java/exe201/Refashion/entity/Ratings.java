package exe201.Refashion.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Ratings")
public class Ratings {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    Users reviewer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Products product;

    @Column(name = "rating")
    Integer rating;

    @Column(name = "comment")
    String comment;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
