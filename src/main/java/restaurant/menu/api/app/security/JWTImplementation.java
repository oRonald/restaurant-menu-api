package restaurant.menu.api.app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import restaurant.menu.api.app.domain.database.entities.Employees;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Component
public class JWTImplementation {

    @Value("${api.security.jwt.secret}")
    private String secret;

    public String generateToken(Employees user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Restaurant")
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS)
                            .atOffset(ZoneOffset.of("-03:00")).toInstant())
                    .sign(algorithm);
        } catch (JWTCreationException e){
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API Restaurant")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
