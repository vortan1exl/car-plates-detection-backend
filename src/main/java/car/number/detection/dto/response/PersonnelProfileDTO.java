package car.number.detection.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@AllArgsConstructor
public class PersonnelProfileDTO {
    @NotBlank
    @Email
    public String email;

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

    public List<VehicleDTO> vehicleDTO;
}
