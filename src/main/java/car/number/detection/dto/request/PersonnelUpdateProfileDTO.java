package car.number.detection.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class PersonnelUpdateProfileDTO {
    @NotBlank
    public String firstName;

    @NotBlank
    public String middleName;

    @NotBlank
    public String lastName;

    @NotBlank
    public String phone;

    @NotBlank
    public String faculty;

    @NotBlank
    public String position;
}
