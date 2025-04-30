package car.number.detection.controller;

import car.number.detection.dto.response.*;
import car.number.detection.service.ParkingService;
import car.number.detection.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final ParkingService parkingService;

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileDTO> getStudentProfile() {
        return ResponseEntity.ok(studentService.getStudentProfile());
    }

    @PostMapping("/update_profile")
    public ResponseEntity<String> updateStudentProfile(@RequestBody StudentProfileDTO dto) {
        return ResponseEntity.ok(studentService.updateStudentProfile(dto));
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
