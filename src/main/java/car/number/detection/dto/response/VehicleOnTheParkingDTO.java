package car.number.detection.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public class VehicleOnTheParkingDTO {
    public UUID id;

    @NotBlank
    public String carPlate;

    @NotBlank
    public String brand;

    @NotBlank
    public String model;

    @NotBlank
    public String color;

    public LocalDateTime entryTime;

    public LocalDateTime exitTime;

    @JsonCreator
    public VehicleOnTheParkingDTO(
            @JsonProperty("id") UUID id,
            @JsonProperty("carPlate") String carPlate,
            @JsonProperty("brand") String brand,
            @JsonProperty("model") String model,
            @JsonProperty("color") String color,
            @JsonProperty("entryTime") LocalDateTime entryTime,
            @JsonProperty("exitTime") LocalDateTime exitTime) {
        this.id = id;
        this.carPlate = carPlate;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }
}

