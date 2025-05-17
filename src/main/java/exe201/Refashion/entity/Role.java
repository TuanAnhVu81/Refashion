package exe201.Refashion.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Role")
public class Role {
    @Id
    @Column(name = "role_id", length = 50)
    String roleId;

    @Column(name = "role_name", nullable = false, length = 50)
    String roleName;

    String description;

    Boolean active;
    @PrePersist
    protected void onCreate() {
        if (active == null) {
            active = true;
        }
    }
}
