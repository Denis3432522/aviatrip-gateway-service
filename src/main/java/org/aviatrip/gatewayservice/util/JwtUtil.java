package org.aviatrip.gatewayservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key SIGNING_KEY;

    @PostConstruct
    private void initMethod() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        SIGNING_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    public Jws<Claims> parseClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);
    }

    public Optional<String> getClaim(String claimName, Jws<Claims> claims) {
        return Optional.ofNullable((String) claims.getBody().get(claimName));
    }
}
