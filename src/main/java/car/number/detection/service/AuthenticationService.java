package car.number.detection.service;

import car.number.detection.dto.request.PersonnelLoginDTO;
import car.number.detection.dto.request.StudentLoginDTO;
import car.number.detection.dto.response.AuthenticationDTO;
import car.number.detection.entity.*;
import car.number.detection.repository.PersonnelRepository;
import car.number.detection.repository.StudentRepository;
import car.number.detection.repository.TokenRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
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
    private final TokenRepository tokenRepository;

    public AuthenticationDTO loginPersonnel(PersonnelLoginDTO dto, HttpServletResponse response) throws IOException {
        Optional<Personnel> optionalPersonnel = personnelRepository.findByEmail(dto.getEmail());

        if (optionalPersonnel.isEmpty()) {
            response.sendError(403, "Не верный логин или пароль.");
            return null;
        }

        Personnel personnel = optionalPersonnel.get();

        if (!passwordEncoder.matches(dto.getPassword(), personnel.getPassword())) {
            response.sendError(403, "Не верный логин или пароль.");
            return null;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        authenticationManager.authenticate(authenticationToken);

        var accessToken = jwtService.generateAccessToken(personnel);
        var refreshToken = jwtService.generateRefreshToken(personnel);

        tokenRepository.save(Token.builder()
                .personnel(personnel)
                .token(refreshToken.getLeft())
                .expires(
                        refreshToken.getRight().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                )
                .build());
        Cookie cookie = new Cookie("refreshToken", refreshToken.getLeft());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth");
        response.addCookie(cookie);
        cookie.setMaxAge(30 * 24 * 60 * 60);

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

    public AuthenticationDTO loginStudent(StudentLoginDTO dto, HttpServletResponse response) throws IOException {
        boolean isValid = otpService.checkOtp(dto.getEmail(), dto.getCode());
        if (!isValid) {
            response.sendError(403, "Не верный логин или код.");
            return null;
        }

        Optional<Student> optionalStudent = studentRepository.findByEmail(dto.getEmail());
        if (optionalStudent.isEmpty()) {
            response.sendError(403, "Не верный логин или код.");
            return null;
        }
        Student student = optionalStudent.get();

        var accessToken = jwtService.generateAccessToken(student);
        var refreshToken = jwtService.generateRefreshToken(student);

        tokenRepository.save(Token.builder()
                .student(student)
                .token(refreshToken.getLeft())
                .expires(
                        refreshToken.getRight().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                )
                .build());
        Cookie cookie = new Cookie("refreshToken", refreshToken.getLeft());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth");
        response.addCookie(cookie);
        cookie.setMaxAge(30 * 24 * 60 * 60);

        return new AuthenticationDTO(accessToken);
    }

    public boolean signOut(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request.getCookies());

        if (!ObjectUtils.isEmpty(refreshToken)) {
            tokenRepository.deleteByToken(refreshToken);
        }

        var cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return true;
    }

    public AuthenticationDTO refreshToken(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {
        String refreshToken = getRefreshToken(request.getCookies());

        if (ObjectUtils.isEmpty(refreshToken)) {
            response.sendError(401, "Отсутствует рефреш токен");
            return null;
        }

        String username = null;

        try {
            username = jwtService.extractUserName(refreshToken);
        } catch (JwtException e) {
            log.debug("JWT Exception: ", e);
        }

        if (ObjectUtils.isEmpty(username)) {
            response.sendError(401, "Недействительный токен обновления");
            return null;
        }

        Optional<Token> tokenOpt = tokenRepository.findByToken(refreshToken);
        if (tokenOpt.isEmpty()) {
            response.sendError(401, "Отсутствует рефреш токен");
            return null;
        }

        Token token = tokenOpt.get();
        UserInterface user = null;

        if (token.getStudent() != null) {
            user = token.getStudent();
        } else if (token.getPersonnel() != null) {
            user = token.getPersonnel();
        }

        if (jwtService.isTokenValid(refreshToken, user)) {
            var accessToken = jwtService.generateAccessToken(user);
            var newRefreshToken = jwtService.generateRefreshToken(user);

            Token newToken = new Token();
            if (user instanceof Student) {
                newToken.setStudent((Student) user);  // Устанавливаем связку с Student
            } else if (user instanceof Personnel) {
                newToken.setPersonnel((Personnel) user);  // Устанавливаем связку с Personnel
            }
            newToken.setToken(newRefreshToken.getLeft());
            newToken.setExpires(newRefreshToken.getRight()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());

            tokenRepository.save(newToken);

            tokenRepository.delete(token);

            Cookie cookie = new Cookie("refreshToken", newRefreshToken.getLeft());
            cookie.setHttpOnly(true);

            cookie.setPath("/api/auth");
            cookie.setMaxAge(30 * 24 * 60 * 60);
            response.addCookie(cookie);

            return new AuthenticationDTO(accessToken);
        } else {
            response.sendError(401, "Invalid refresh token");
            return null;
        }
    }

    private static String getRefreshToken(Cookie[] cookies) {
        if (ObjectUtils.isEmpty(cookies)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void createPersonnel(String email, String rawPassword) {
        if (personnelRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует.");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        Personnel personnel = new Personnel();
        personnel.setEmail(email);
        personnel.setPassword(encodedPassword);
        personnel.setIsAdmin(true);

        personnelRepository.save(personnel);

        System.out.println("✅ Пользователь успешно создан: " + email);
    }
}
