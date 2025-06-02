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
@Table(name = "Subscriptions")
public class Subscriptions {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @Column(name = "package_type")
    String packageType;

    @Column(name = "message_limit")
    Integer messageLimit;

    @Column(name = "start_date")
    LocalDateTime startDate;

    @Column(name = "end_date")
    LocalDateTime endDate;

    @Column(name = "status")
    String status;
}
