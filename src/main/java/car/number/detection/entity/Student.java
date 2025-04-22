package car.number.detection.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "student")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student implements UserInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String middleName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 13)
    private String phone;

    @Column(length = 10)
    private String student_card;

    @Column(length = 100)
    private String faculty;

    @Column(length = 2)
    private int course;

    @Column(length = 10)
    private String groups;

    @OneToMany(mappedBy = "student", targetEntity = Vehicle.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Vehicle> vehicles;

    public Role getRole(){
        return Role.STUDENT;
    }

    @Override
    public String getUsername(){
        return getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Role.STUDENT.getAuthorities();
    }

    @Override
    public String getPassword() {
        return "";
    }
}
