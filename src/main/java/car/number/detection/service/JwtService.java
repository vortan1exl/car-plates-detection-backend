package car.number.detection.service;

import car.number.detection.entity.Personnel;
import car.number.detection.entity.Role;
import car.number.detection.entity.Student;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    @Value("${jwt.secret-key.private}")
    private String jwtPrivateKey;
    @Value("${jwt.secret-key.public}")
    private String jwtPublicKey;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractType(String token) {
        return extractClaim(token, v -> v.get("type")).toString();
    }

    public Role extractRole(String token){
        return extractClaim(token, v -> {
            String z = v.get("role").toString();
            try {
                return Role.valueOf(z);
            }
            catch (IllegalArgumentException e){
                return Role.UNKNOWN;
            }
        });
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        long live = 2L * 60 * 60 * 1000;
        if (userDetails instanceof Personnel customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("role", customUserDetails.getRole());
        } else if (userDetails instanceof Student customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("role", Role.STUDENT);
            live = 10L  * 60 * 1000;
        }
        claims.put("type", "access");

        long issuedAt = System.currentTimeMillis();

        return generateToken(claims, userDetails, new Date(issuedAt), new Date(issuedAt + live));
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        long issuedAt = System.currentTimeMillis();
        Date expiration = new Date(issuedAt + 30L * 24 * 60 * 60 * 1000);

        return generateToken(claims, userDetails, new Date(issuedAt), expiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Date issuedAt, Date expiration){
        return Jwts.builder()
                .claims(extraClaims).subject(userDetails.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private RSAPublicKey getPublicKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtPublicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        }
        catch (Exception e){
            return null;
        }
    }

    private RSAPrivateKey getPrivateKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtPrivateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(spec);
        }
        catch (Exception e){
            return null;
        }
    }


}
