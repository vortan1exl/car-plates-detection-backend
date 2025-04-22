package car.number.detection.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "personnel")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Personnel implements UserInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 150)
    private String password;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String middleName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 13)
    private String phone;

    @Column(length = 100)
    private String faculty;

    @Column(length = 100)
    private String position;

    public Role getRole(){
        return Role.PERSONNEL;
    }

    @OneToMany(mappedBy = "personnel", targetEntity = Vehicle.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Vehicle> vehicles;

    @Override
    public String getUsername(){
        return getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Role.PERSONNEL.getAuthorities();
    }

    public String getPassword() {
        return "";
    }
}
