package car.number.detection.controller;

import car.number.detection.dto.request.PersonnelLoginDTO;
import car.number.detection.dto.response.AuthenticationDTO;
import car.number.detection.dto.response.StudentProfileDTO;
import car.number.detection.service.AuthenticationService;
import car.number.detection.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/student_profile")
    public ResponseEntity<StudentProfileDTO> getStudentProfile() throws IOException {
        return ResponseEntity.ok(studentService.getStudentProfile());
    }
}
