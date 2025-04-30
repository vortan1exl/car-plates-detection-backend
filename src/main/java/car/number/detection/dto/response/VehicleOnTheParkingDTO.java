package car.number.detection.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class VehicleOnTheParkingDTO {
    public UUID id;
    @NotBlank
    public String carPlate;

    @NotBlank
    public String brand;

    @NotBlank
    public String model;

    @NotBlank
    public String color;

    LocalDateTime entryTime;

    LocalDateTime exitTime;
}

