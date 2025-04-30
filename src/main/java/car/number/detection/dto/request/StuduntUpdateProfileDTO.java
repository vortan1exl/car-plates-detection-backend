package car.number.detection.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class StuduntUpdateProfileDTO {
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

}
