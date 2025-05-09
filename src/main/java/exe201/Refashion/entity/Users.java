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
    @Column(name = "id")
    String id;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "role", columnDefinition = "VARCHAR(20) CHECK (role IN ('buyer', 'seller', 'admin')) DEFAULT 'buyer'")
    String role;

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
}
