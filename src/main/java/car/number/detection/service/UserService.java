package car.number.detection.service;

import car.number.detection.entity.Student;
import car.number.detection.entity.UserInterface;
import car.number.detection.repository.PersonnelRepository;
import car.number.detection.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import car.number.detection.entity.Role;



import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PersonnelRepository personnelRepository;
    private final StudentRepository studentRepository;

    public static Optional<UserInterface> getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserInterface)
                .map(UserInterface.class::cast);
    }

    public UserInterface getByUsernameAndRole(String username, Role role) throws UsernameNotFoundException {
        if(Role.STUDENT.equals(role)){
            return studentRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
        }
        if(Role.UNKNOWN.equals(role)){
            throw new UsernameNotFoundException("Bad credentials");
        }

        return personnelRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }

    public UserInterface getByUsername(String username) throws UsernameNotFoundException {

        Optional<Student> userInterface = studentRepository.findByEmail(username);
        if(userInterface.isPresent()){
            return userInterface.get();
        }
        return personnelRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}