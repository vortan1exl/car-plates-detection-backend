package car.number.detection.service;

import car.number.detection.dto.response.PersonnelProfileDTO;
import car.number.detection.dto.response.StudentProfileDTO;
import car.number.detection.dto.response.VehicleDTO;
import car.number.detection.entity.Personnel;
import car.number.detection.entity.Student;
import car.number.detection.entity.Vehicle;
import car.number.detection.repository.StudentRepository;
import car.number.detection.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final VehicleRepository vehicleRepository;

    public StudentProfileDTO getStudentProfile(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        List<Vehicle> vehicleList = vehicleRepository.findByStudent(student);

        List<VehicleDTO> dtoList = vehicleList.stream()
                .map(vehicle -> new VehicleDTO(
                        vehicle.getId(),
                        vehicle.getCarPlate(),
                        vehicle.getBrand(),
                        vehicle.getModel(),
                        vehicle.getColor()
                ))
                .toList();

        return new StudentProfileDTO(
                student.getEmail(),
                student.getFirstName(),
                student.getMiddleName(),
                student.getLastName(),
                student.getPhone(),
                student.getStudent_card(),
                student.getFaculty(),
                student.getCourse(),
                student.getGroups(),
                dtoList
        );
    }
    public String updateStudentProfile(StudentProfileDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));

        student.setFirstName(dto.getFirstName());
        student.setMiddleName(dto.getMiddleName());
        student.setLastName(dto.getLastName());
        student.setPhone(dto.getPhone());
        student.setStudent_card(dto.getStudent_card());
        student.setFaculty(dto.getFaculty());
        student.setCourse(dto.getCourse());
        student.setGroups(dto.getGroups());

        studentRepository.save(student);

        return "Данные профиля успешно обновлены!";
    }
}
