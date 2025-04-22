package car.number.detection.controller;

import car.number.detection.dto.request.PersonnelLoginDTO;
import car.number.detection.dto.request.StudentLoginDTO;
import car.number.detection.dto.request.StudentSignDTO;
import car.number.detection.dto.response.AuthenticationDTO;
import car.number.detection.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/admin/login")
    public ResponseEntity<AuthenticationDTO> loginPersonnel(@RequestBody @Valid PersonnelLoginDTO dto, HttpServletResponse response) throws IOException {
        AuthenticationDTO authenticationDTOResponseEntity = authenticationService.loginPersonnel(dto, response);
        return ResponseEntity.ok(authenticationDTOResponseEntity);
    }

    @PostMapping("/student")
    public void createCode(@RequestBody @Valid StudentSignDTO dto){
        authenticationService.createCode(dto.email);
    }

    @PostMapping("/student/login")
    public ResponseEntity<AuthenticationDTO> loginStudent(@RequestBody @Valid StudentLoginDTO dto, HttpServletResponse response) throws IOException{
        AuthenticationDTO authenticationDTOResponseEntity = authenticationService.loginStudent(dto, response);
        return ResponseEntity.ok(authenticationDTOResponseEntity);
    }

    @PostMapping("/sign-out")
    public boolean loginStudent(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        return authenticationService.signOut(request, response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationDTO> refreshToken(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(authenticationService.refreshToken(request, response));
    }
}
