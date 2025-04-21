package car.number.detection.service;

import car.number.detection.dto.request.PersonnelLoginDTO;
import car.number.detection.dto.request.StudentLoginDTO;
import car.number.detection.dto.response.AuthenticationDTO;
import car.number.detection.entity.Personnel;
import car.number.detection.entity.Student;
import car.number.detection.repository.PersonnelRepository;
import car.number.detection.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final PersonnelRepository personnelRepository;
    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailCode;

    public AuthenticationDTO loginPersonnel(PersonnelLoginDTO dto) {
        Optional<Personnel> optionalPersonnel = personnelRepository.findByEmail(dto.getEmail());
        if (optionalPersonnel.isEmpty()) {
            return new AuthenticationDTO("Не верный логин или пароль");
        }

        Personnel personnel = optionalPersonnel.get();

        if (!passwordEncoder.matches(dto.getPassword(), personnel.getPassword())) {
            return new AuthenticationDTO("Не верный логин или пароль");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtService.generateAccessToken(personnel);

        return new AuthenticationDTO(accessToken);
    }

    public void createCode(String email){
        Student student = studentRepository.findByEmail(email).orElseGet(
                () -> studentRepository.save(
                        Student.builder()
                                .email(email)
                                .build()
                )
        );
        String code = otpService.generateOtp(email);

        emailCode.emailCode(code);
    }

    public AuthenticationDTO loginStudent(StudentLoginDTO dto){
        boolean isValid = otpService.checkOtp(dto.getEmail(), dto.getCode());
        if (!isValid) {
            return new AuthenticationDTO("Не верный логин или код");
        }

        Optional<Student> optionalStudent = studentRepository.findByEmail(dto.getEmail());
        if (optionalStudent.isEmpty()) {
            return new AuthenticationDTO("Не верный логин или код");
        }

        Student student = optionalStudent.get();
        String accessToken = jwtService.generateAccessToken(student);

        return new AuthenticationDTO(accessToken);
    }


}
