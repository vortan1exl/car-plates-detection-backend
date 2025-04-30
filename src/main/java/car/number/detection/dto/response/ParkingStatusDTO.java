package car.number.detection.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class ParkingStatusDTO {
    @NotBlank
    public String status;

    @NotBlank
    public int countCar;
}
