package car.number.detection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {
    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private CacheManager cacheManager;

    @CachePut(value = "otp", key = "#email")
    public String generateOtp(String email){
        return String.valueOf(100_000 + secureRandom.nextInt(900_000));
    }

    public boolean checkOtp(String email, String code){
        Cache cache = cacheManager.getCache("otp");
        if(cache == null || code == null) return false;

        String cachedCode = cache.get(email, String.class);
        if (code.equals(cachedCode)) {
            cache.evict(email);
            return true;
        }
        return false;
    }
}