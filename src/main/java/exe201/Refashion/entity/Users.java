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
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "address")
    String address;

    @Column(name = "profile_picture")
    String profilePicture;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "verification_token")
    String verificationToken;

    Boolean emailVerified;
    Boolean active;
    @PrePersist
    protected void onCreate() {
        if (active == null) {
            active = true;
        }
        if (emailVerified == null) {
            emailVerified = false;  // Mặc định chưa xác thực
        }
    }
}
