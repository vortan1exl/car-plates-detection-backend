package car.number.detection.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StudentDTOV {
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
    public String student_card;

    @NotBlank
    public String faculty;

    @NotBlank
    public int course;

    @NotBlank
    public String groups;
}
