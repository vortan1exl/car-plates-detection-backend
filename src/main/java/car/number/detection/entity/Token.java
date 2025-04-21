package car.number.detection.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @Column(nullable = false, length = 512)
    private String token;

    @ManyToOne(targetEntity = Student.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "personnel_id")
    private Personnel personnel;

    @Column(nullable = false)
    private LocalDateTime expires;
}

