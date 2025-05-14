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
@Table(name = "Cart")
public class Cart {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    Users user;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
