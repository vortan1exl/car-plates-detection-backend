package car.number.detection.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;

@Value
@Getter
@Setter
@AllArgsConstructor
public class VehicleParkingHistoryDTO {
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
