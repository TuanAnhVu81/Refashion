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
@Table(name = "Categories")
public class Categories {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description")
    String description;
}