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
@Table(name = "BlogComments")
public class BlogComment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    Blog blog;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    String content;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
