package car.number.detection.service;

import car.number.detection.dto.response.PersonnelProfileDTO;
import car.number.detection.dto.response.VehicleDTO;
import car.number.detection.entity.Personnel;
import car.number.detection.entity.Vehicle;
import car.number.detection.repository.PersonnelRepository;
import car.number.detection.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonnelService {
    private final PersonnelRepository personnelRepository;
    private final VehicleRepository vehicleRepository;

    public PersonnelProfileDTO getPersonnelProfile(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Personnel personnel = personnelRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Персонал не найден"));

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

        return new PersonnelProfileDTO(
                personnel.getEmail(),
                personnel.getFirstName(),
                personnel.getMiddleName(),
                personnel.getLastName(),
                personnel.getPhone(),
                personnel.getFaculty(),
                personnel.getPosition(),
                dtoList
        );
    }

    public String updatePersonnelProfile(PersonnelProfileDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Personnel personnel = personnelRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Персонал не найден"));

        personnel.setFirstName(dto.getFirstName());
        personnel.setMiddleName(dto.getMiddleName());
        personnel.setLastName(dto.getLastName());
        personnel.setPhone(dto.getPhone());
        personnel.setFaculty(dto.getFaculty());
        personnel.setPosition(dto.getPosition());

        personnelRepository.save(personnel);

        return "Данные профиля успешно обновлены!";
    }
}
