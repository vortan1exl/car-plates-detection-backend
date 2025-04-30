package car.number.detection.controller;

import car.number.detection.dto.response.ParkingStatusDTO;
import car.number.detection.dto.response.PersonnelProfileDTO;
import car.number.detection.dto.response.VehicleParkingHistoryDTO;
import car.number.detection.dto.response.VehicleParkingStatusDTO;
import car.number.detection.service.ParkingService;
import car.number.detection.service.PersonnelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personnel")
@RequiredArgsConstructor
public class PersonnelController {
    private final PersonnelService personnelService;
    private final ParkingService parkingService;

    @GetMapping("/profile")
    public ResponseEntity<PersonnelProfileDTO> getPersonnelProfile() {
        return ResponseEntity.ok(personnelService.getPersonnelProfile());
    }

    @PostMapping("/update_profile")
    public ResponseEntity<String> updatePersonnelProfile(@RequestBody PersonnelProfileDTO dto){
        return ResponseEntity.ok(personnelService.updatePersonnelProfile(dto));
    }

    @GetMapping("/status_parking")
    public ResponseEntity<ParkingStatusDTO> getInfoAboutParking(){
        return ResponseEntity.ok(parkingService.getParkingInfo());
    }

    @GetMapping("/vehicle_status")
    public ResponseEntity<List<VehicleParkingStatusDTO>> getInfoAboutVehicleStatus(){
        return ResponseEntity.ok(parkingService.getInfoAboutVehicleStatus());
    }

    @GetMapping("/vehicle_history")
    public ResponseEntity<List<VehicleParkingHistoryDTO>> getVehicleParkingHistory(){
        return ResponseEntity.ok(parkingService.getVehicleParkingHistory());
    }

    @GetMapping("/visit_count")
    public ResponseEntity<Integer> getCountParkingVisitsThisMonth(){
        return ResponseEntity.ok(parkingService.getCountParkingVisitsThisMonth());
    }
    @GetMapping("/average_visit_time")
    public ResponseEntity<Double> calculateAverageParkingDuration(){
        return ResponseEntity.ok(parkingService.calculateAverageParkingDuration());
    }

}
