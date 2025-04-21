package car.number.detection.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
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

    public Role getRole(){
        return Role.PERSONNEL;
    }

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
