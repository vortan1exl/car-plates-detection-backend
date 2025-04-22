package car.number.detection.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class StudentProfileDTO {
    @NotBlank
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
    public String student_card;

    @NotBlank
    public String faculty;

    @NotBlank
    public int course;

    @NotBlank
    public String groups;

    public List<VehicleDTO> vehicleDTO;
}
