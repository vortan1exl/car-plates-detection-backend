package car.number.detection.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PersonnelDTO {
    @NotBlank
    public UUID id;

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

    public boolean isAdmin;

    public List<VehicleDTO> vehicleDTOList;

}
