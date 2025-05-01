package car.number.detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableKafka
@SpringBootApplication
public class CarPlatesDetectionBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarPlatesDetectionBackendApplication.class, args);
	}
}
