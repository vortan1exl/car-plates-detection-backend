package car.number.detection.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class VehicleDTO {
    public UUID id;

    @NotBlank
    public String carPlate;

    @NotBlank
    public String brand;

    @NotBlank
    public String model;

    @NotBlank
    public String color;
}
