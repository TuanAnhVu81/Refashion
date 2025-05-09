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
@Table(name = "Messages")
public class Messages {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    Users sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    Users receiver;

    @Column(name = "message")
    String message;

    @Column(name = "sent_at")
    LocalDateTime sentAt;

    @Column(name = "is_read")
    Boolean isRead;
}
