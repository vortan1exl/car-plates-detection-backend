package car.number.detection.repository;


import car.number.detection.entity.ParkingLog;
import car.number.detection.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ParkingLogRepository extends JpaRepository<ParkingLog, Long> {
    List<ParkingLog> findByExitTimeIsNull();

    boolean existsByVehicleAndExitTimeIsNull(Vehicle vehicle);

    List<ParkingLog> findByVehicleOrderByEntryTimeDesc(Vehicle vehicle);

    Integer countByVehicleInAndEntryTimeBetween(List<Vehicle> vehicles, LocalDateTime startOfMonth, LocalDateTime endOfMonth);

    List<ParkingLog> findByVehicleIn(Collection<Vehicle> vehicles);
}
