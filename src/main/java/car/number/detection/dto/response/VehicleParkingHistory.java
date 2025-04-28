package car.number.detection.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class VehicleParkingHistory {
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

    Duration duration;
}
