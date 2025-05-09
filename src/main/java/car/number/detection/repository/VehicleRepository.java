package car.number.detection.repository;

import car.number.detection.entity.Personnel;
import car.number.detection.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import car.number.detection.entity.Vehicle;
import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    List<Vehicle> findByStudent(Student student);
    List<Vehicle> findByPersonnel(Personnel personnel);

    Vehicle findByCarPlate(String carPlate);
}