package car.number.detection.service;

import car.number.detection.dto.response.*;
import car.number.detection.entity.ParkingLog;
import car.number.detection.entity.Personnel;
import car.number.detection.entity.Student;
import car.number.detection.entity.Vehicle;
import car.number.detection.repository.ParkingLogRepository;
import car.number.detection.repository.PersonnelRepository;
import car.number.detection.repository.StudentRepository;
import car.number.detection.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {
    private final PersonnelRepository personnelRepository;
    private final VehicleRepository vehicleRepository;
    private final StudentRepository studentRepository;
    private final ParkingLogRepository parkingLogRepository;

    public List<StudentDTO> getAllStudent(){
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> {
                    StudentDTO dto = new StudentDTO();
                    dto.id = student.getId();
                    dto.email = student.getEmail();
                    dto.firstName = student.getFirstName();
                    dto.middleName = student.getMiddleName();
                    dto.lastName = student.getLastName();
                    dto.phone = student.getPhone();
                    dto.student_card = student.getStudent_card();
                    dto.faculty = student.getFaculty();
                    dto.course = student.getCourse();
                    dto.groups = student.getGroups();
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<PersonnelDTO> getAllPersonnel(){
        List<Personnel> personnels = personnelRepository.findAll();
        return personnels.stream()
                .map(personnel -> {
                    PersonnelDTO dto = new PersonnelDTO();
                    dto.id = personnel.getId();
                    dto.email = personnel.getEmail();
                    dto.firstName = personnel.getFirstName();
                    dto.middleName = personnel.getMiddleName();
                    dto.lastName = personnel.getLastName();
                    dto.phone = personnel.getPhone();
                    dto.faculty = personnel.getFaculty();
                    dto.position = personnel.getPosition();
                    dto.isAdmin = personnel.getIsAdmin();
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(UUID id){
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студент не найден:" + id));

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


        StudentDTO dto = new StudentDTO();
        dto.id = student.getId();
        dto.email = student.getEmail();
        dto.firstName = student.getFirstName();
        dto.middleName = student.getMiddleName();
        dto.lastName = student.getLastName();
        dto.phone = student.getPhone();
        dto.student_card = student.getStudent_card();
        dto.faculty = student.getFaculty();
        dto.course = student.getCourse();
        dto.groups = student.getGroups();
        dto.vehicleDTO = dtoList;

        return dto;
    }

    public PersonnelDTO getPersonnelById(UUID id){
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Персонал не найден:" + id));

        List<Vehicle> vehicleList = vehicleRepository.findByPersonnel(personnel);

        List<VehicleDTO> dtoList = vehicleList.stream()
                .map(vehicle -> new VehicleDTO(
                        vehicle.getId(),
                        vehicle.getCarPlate(),
                        vehicle.getBrand(),
                        vehicle.getModel(),
                        vehicle.getColor()
                ))
                .toList();

        PersonnelDTO dto = new PersonnelDTO();
        dto.id = personnel.getId();
        dto.email = personnel.getEmail();
        dto.firstName = personnel.getFirstName();
        dto.middleName = personnel.getMiddleName();
        dto.lastName = personnel.getLastName();
        dto.phone = personnel.getPhone();
        dto.faculty = personnel.getFaculty();
        dto.position = personnel.getPosition();
        dto.isAdmin = personnel.getIsAdmin();
        dto.vehicleDTOList = dtoList;


        return dto;
    }

    public String updatePersonnelById(UUID id, PersonnelDTO dto) {
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Персонал не найден"));

        personnel.setFirstName(dto.getFirstName());
        personnel.setMiddleName(dto.getMiddleName());
        personnel.setLastName(dto.getLastName());
        personnel.setPhone(dto.getPhone());
        personnel.setFaculty(dto.getFaculty());
        personnel.setPosition(dto.getPosition());

        personnelRepository.save(personnel);

        return "Данные успешно обновлены!";

    }

    public String updateStudentById(UUID id, StudentDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));

        student.setFirstName(dto.getFirstName());
        student.setMiddleName(dto.getMiddleName());
        student.setLastName(dto.getLastName());
        student.setPhone(dto.getPhone());
        student.setFaculty(dto.getFaculty());
        student.setStudent_card(dto.getStudent_card());
        student.setCourse(dto.getCourse());
        student.setGroups(dto.getGroups());


        studentRepository.save(student);

        return "Данные успешно обновлены!";

    }

    public String addVehicleForStudent(UUID id, VehicleDTO dto){
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setColor(dto.getColor());
        vehicle.setCarPlate(dto.getCarPlate());
        vehicle.setStudent(student);

        vehicleRepository.save(vehicle);

        return "Авто успешно добавлено";
    }

    public String addVehicleForPersonnel(UUID id, VehicleDTO dto){
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Персонал не найден"));

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setColor(dto.getColor());
        vehicle.setCarPlate(dto.getCarPlate());
        vehicle.setPersonnel(personnel);

        vehicleRepository.save(vehicle);

        return "Авто успешно добавлено";
    }

    public List<VehicleOnTheParkingDTO> getVehicleOnTheParking(){
        List<ParkingLog> activeLogs = parkingLogRepository.findByExitTimeIsNull();

        return activeLogs.stream()
                .map(log -> {
                    Vehicle vehicle = log.getVehicle();
                    return new VehicleOnTheParkingDTO(
                            vehicle.getId(),
                            vehicle.getBrand(),
                            vehicle.getModel(),
                            vehicle.getColor(),
                            vehicle.getCarPlate(),
                            log.getEntryTime(),
                            log.getExitTime()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<VehicleOnTheParkingDTO> getHistoryOnTheParking(){
        List<ParkingLog> activeLogs = parkingLogRepository.findAll();

        return activeLogs.stream()
                .map(log -> {
                    Vehicle vehicle = log.getVehicle();
                    return new VehicleOnTheParkingDTO(
                            vehicle.getId(),
                            vehicle.getCarPlate(),
                            vehicle.getBrand(),
                            vehicle.getModel(),
                            vehicle.getColor(),
                            log.getEntryTime(),
                            log.getExitTime()
                    );
                })
                .collect(Collectors.toList());
    }

}
