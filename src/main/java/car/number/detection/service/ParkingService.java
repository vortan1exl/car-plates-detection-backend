package car.number.detection.service;

import car.number.detection.dto.response.ParkingStatusDTO;
import car.number.detection.dto.response.VehicleParkingHistory;
import car.number.detection.dto.response.VehicleParkingStatusDTO;
import car.number.detection.entity.ParkingLog;
import car.number.detection.entity.Personnel;
import car.number.detection.entity.Student;
import car.number.detection.entity.Vehicle;
import car.number.detection.repository.ParkingLogRepository;
import car.number.detection.repository.PersonnelRepository;
import car.number.detection.repository.StudentRepository;
import car.number.detection.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.YearMonth;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Service
@AllArgsConstructor
public class ParkingService {
    private final ParkingLogRepository parkingLogRepository;
    private final StudentRepository studentRepository;
    private final PersonnelRepository personnelRepository;
    private final VehicleRepository vehicleRepository;

    public ParkingStatusDTO getParkingInfo() {
        List<ParkingLog> parkingLogList = parkingLogRepository.findByExitTimeIsNull();
        int countCar = parkingLogList.size();
        String status = "Парковка свободна";
        if (countCar >= 30) status = "Парковка заполнена";
        return new ParkingStatusDTO(
                status,
                countCar
        );
    }

    public List<VehicleParkingStatusDTO> getInfoAboutVehicleStatus() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<VehicleParkingStatusDTO> result = new ArrayList<>();
        List<Vehicle> vehicleList = null;
        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            vehicleList = vehicleRepository.findByStudent(student);
        } else {
            Personnel personnel = personnelRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Ни студент, ни персонал не найдены"));
            vehicleList = vehicleRepository.findByPersonnel(personnel);
        }
        for (Vehicle vehicle : vehicleList) {
            boolean isParked = parkingLogRepository.existsByVehicleAndExitTimeIsNull(vehicle);
            String status = isParked ? "На парковке" : "Отсутствует";

            result.add(new VehicleParkingStatusDTO(
                    vehicle.getCarPlate(),
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getColor(),
                    status
            ));

        }
        return result;
    }

    public List<VehicleParkingHistory> getVehicleParkingHistory(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Vehicle> vehicleList;
        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            vehicleList = vehicleRepository.findByStudent(student);
        } else {
            Personnel personnel = personnelRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Ни студент, ни персонал не найдены"));
            vehicleList = vehicleRepository.findByPersonnel(personnel);
        }

        List<VehicleParkingHistory> history = new ArrayList<>();

        for (Vehicle vehicle : vehicleList) {
            List<ParkingLog> logs = parkingLogRepository.findByVehicleOrderByEntryTimeDesc(vehicle);
            for (ParkingLog log : logs) {
                Duration duration = (log.getExitTime() != null)
                        ? Duration.between(log.getEntryTime(), log.getExitTime())
                        : Duration.between(log.getEntryTime(), LocalDateTime.now());

                history.add(new VehicleParkingHistory(
                        vehicle.getCarPlate(),
                        vehicle.getBrand(),
                        vehicle.getModel(),
                        vehicle.getColor(),
                        log.getEntryTime(),
                        log.getExitTime(),
                        duration
                ));
            }

        }
        return history;
    }

    public Integer getCountParkingVisitsThisMonth() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Vehicle> vehicles;

        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            vehicles = vehicleRepository.findByStudent(student);
        } else {
            Personnel personnel = personnelRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Ни студент, ни персонал не найдены"));
            vehicles = vehicleRepository.findByPersonnel(personnel);
        }

        if (vehicles.isEmpty()) return 0;

        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return parkingLogRepository.countByVehicleInAndEntryTimeBetween(vehicles, startOfMonth, endOfMonth);
    }

    public double calculateAverageParkingDuration() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Vehicle> vehicles;

        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            vehicles = vehicleRepository.findByStudent(student);
        } else {
            Personnel personnel = personnelRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Ни студент, ни персонал не найдены"));
            vehicles = vehicleRepository.findByPersonnel(personnel);
        }

        if (vehicles.isEmpty()) return 0.0;

        List<ParkingLog> logs = parkingLogRepository.findByVehicleIn(vehicles);

        OptionalDouble averageDuration = logs.stream()
                .filter(log -> log.getExitTime() != null)
                .mapToLong(log -> Duration.between(log.getEntryTime(), log.getExitTime()).toMinutes()) // Вычисляем длительность в минутах
                .average();

        return averageDuration.orElse(0.0);
    }
}
