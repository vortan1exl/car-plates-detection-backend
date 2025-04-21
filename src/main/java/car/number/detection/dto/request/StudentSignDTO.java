package car.number.detection.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class StudentSignDTO {
    @NotBlank
    @Email
    public String email;
}
