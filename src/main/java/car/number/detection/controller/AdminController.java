package car.number.detection.controller;

import car.number.detection.dto.response.*;
import car.number.detection.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/all_student")
    public ResponseEntity<List<StudentDTO>> getAllStudent(){
        return ResponseEntity.ok(adminService.getAllStudent());
    }

    @GetMapping("/all_personnel")
    public ResponseEntity<List<PersonnelDTO>> getAllPersonnel(){
        return ResponseEntity.ok(adminService.getAllPersonnel());
    }

    @GetMapping("/student/{uuid}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable UUID uuid){
        return ResponseEntity.ok(adminService.getStudentById(uuid));
    }

    @GetMapping("/personnel/{uuid}")
    public ResponseEntity<PersonnelDTO> getPersonnelById(@PathVariable UUID uuid){
        return ResponseEntity.ok(adminService.getPersonnelById(uuid));
    }

    @PostMapping("/update_student/{uuid}")
    public String updateStudentById(@PathVariable UUID uuid, @RequestBody StudentDTOV dto){
        return adminService.updateStudentById(uuid,dto);
    }

    @PostMapping("/update_personnel/{uuid}")
    public String updatePersonnelById(@PathVariable UUID uuid, @RequestBody PersonnelDTOV dto){
        return adminService.updatePersonnelById(uuid, dto);
    }

    @PostMapping("/add_vehicle_for_student/{uuid}")
    public String addVehicleForStudent(@PathVariable UUID uuid, @RequestBody VehicleDTO dto){
        return adminService.addVehicleForStudent(uuid, dto);
    }

    @PostMapping("/add_vehicle_for_personnel/{uuid}")
    public String addVehicleForPersonnel(@PathVariable UUID uuid, @RequestBody VehicleDTO dto){
        return adminService.addVehicleForPersonnel(uuid, dto);
    }

    @GetMapping("/get_vehicle_parking")
    public ResponseEntity<List<VehicleOnTheParkingDTO>> getVehicleOnTheParking(){
        return ResponseEntity.ok(adminService.getVehicleOnTheParking());
    }

    @GetMapping("/get_vehicle_history")
    public ResponseEntity<List<VehicleOnTheParkingDTO>> getHistoryOnTheParking(){
        return ResponseEntity.ok(adminService.getHistoryOnTheParking());
    }

    @PostMapping("/update_vehicle/{uuid}")
    public String updateVehicle(@PathVariable UUID uuid, @RequestBody VehicleDTO dto){
        return adminService.updateVehicle(uuid, dto);
    }

    @DeleteMapping("/delete_vehicle/{uuid}")
    public String deleteVehicle(@PathVariable UUID uuid){
        return adminService.deleteVehicle(uuid);
    }
}
