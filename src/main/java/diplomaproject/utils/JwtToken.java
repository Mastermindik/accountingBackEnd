package diplomaproject.utils;

import diplomaproject.models.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateToken(Account account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", account.getEmail());
        claims.put("username", account.getUsername());

        Date issueDate = new Date();
        Date expiredDate = new Date(issueDate.getTime() + lifetime.toMillis());
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(account.getRoles().name())
                .setIssuedAt(issueDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getRoles(String token) {
        return getAllClaims(token).getSubject();
    }

    public String getEmail(String token) {
        return getAllClaims(token).get("email", String.class);
    }

    public Claims getAllClaims(String token) {
        return  Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
