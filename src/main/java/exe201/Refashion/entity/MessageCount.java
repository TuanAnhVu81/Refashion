package exe201.Refashion.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "MessageCount")
public class MessageCount {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    Users buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    Users seller;

    @Column(name = "month_year")
    String monthYear;

    @Column(name = "message_count")
    Integer messageCount;
}
