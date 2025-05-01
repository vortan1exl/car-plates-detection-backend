package car.number.detection.service;

import car.number.detection.dto.kafka.KafkaPlateMessage;
import car.number.detection.entity.ParkingLog;
import car.number.detection.entity.Vehicle;
import car.number.detection.repository.ParkingLogRepository;
import car.number.detection.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class KafkaListenerService {
    private final VehicleRepository vehicleRepository;
    private final ParkingLogRepository parkingLogRepository;

    @KafkaListener(topics = "car-plates-topic", groupId = "car-monitor-group")
    public void listen(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            KafkaPlateMessage dto = mapper.readValue(message, KafkaPlateMessage.class);
            System.out.println("Номер авто: " + dto.getPlate() + ", время: " + dto.getTimestamp());

            Vehicle vehicle = vehicleRepository.findByCarPlate(dto.getPlate());

            if(vehicle != null){
                ParkingLog parkingLog = parkingLogRepository.findTopByVehicleOrderByEntryTimeDesc(vehicle);
                if(parkingLog != null){
                    if (parkingLog.getExitTime() == null) {
                        parkingLog.setExitTime(dto.getTimestamp());
                        parkingLogRepository.save(parkingLog);
                        System.out.println("Обновлен exit time для автомобиля " + dto.getPlate());

                    } else {
                        ParkingLog newParkingHistory = new ParkingLog();
                        newParkingHistory.setVehicle(vehicle);
                        newParkingHistory.setEntryTime(dto.getTimestamp());
                        parkingLogRepository.save(newParkingHistory);
                        System.out.println("Добавлен новый лог парковки для автомобиля " + dto.getPlate());
                    }
                }
                else{
                    ParkingLog newParkingHistory = new ParkingLog();
                    newParkingHistory.setVehicle(vehicle);
                    newParkingHistory.setEntryTime(dto.getTimestamp());
                    parkingLogRepository.save(newParkingHistory);
                    System.out.println("Добавлен новый лог парковки для автомобиля " + dto.getPlate());
                }

            }
            else{
                System.out.println("Автомобиль с номером " + dto.getPlate() + " не найден.");
            }


        } catch (Exception e) {
            System.err.println("Ошибка при обработке сообщения: " + e.getMessage());
        }
    }
}

