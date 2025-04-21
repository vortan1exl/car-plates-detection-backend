package car.number.detection.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    public void emailCode(String code){
        System.out.println("Код для входа: " + code);
    }
}
