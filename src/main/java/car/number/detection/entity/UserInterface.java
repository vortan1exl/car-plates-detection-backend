package car.number.detection.entity;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.UUID;


public interface UserInterface extends UserDetails {
    UUID getId();
    Role getRole();
}
