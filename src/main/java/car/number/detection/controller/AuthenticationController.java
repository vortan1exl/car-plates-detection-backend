package car.number.detection.controller;

import car.number.detection.dto.request.PersonnelLoginDTO;
import car.number.detection.dto.request.StudentLoginDTO;
import car.number.detection.dto.request.StudentSignDTO;
import car.number.detection.dto.response.AuthenticationDTO;
import car.number.detection.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/admin/login")
    public ResponseEntity<AuthenticationDTO> loginPersonnel(@RequestBody @Valid PersonnelLoginDTO dto) {
        AuthenticationDTO authenticationDTOResponseEntity = authenticationService.loginPersonnel(dto);
        return ResponseEntity.ok(authenticationDTOResponseEntity);
    }

    @PostMapping("/client")
    public void createCode(@RequestBody @Valid StudentSignDTO dto){
        authenticationService.createCode(dto.email);
    }

    @PostMapping("/client/login")
    public ResponseEntity<AuthenticationDTO> loginStudent(@RequestBody @Valid StudentLoginDTO dto) {
        AuthenticationDTO authenticationDTOResponseEntity = authenticationService.loginStudent(dto);
        return ResponseEntity.ok(authenticationDTOResponseEntity);
    }
}
