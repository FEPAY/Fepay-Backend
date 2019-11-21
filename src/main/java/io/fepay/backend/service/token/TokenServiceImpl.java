package io.fepay.backend.service.token;

import io.fepay.backend.exception.InvalidTokenSignatureException;
import io.fepay.backend.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${JWT_ACCESS_EXP}")
    private Long accessExp;

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getIdentity(String jwt) {
        try {
            Claims parsed = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();
            if (parsed.getExpiration().before(Date.from(Instant.now()))) {
                throw new TokenExpiredException("Token Expired");
            }
            return parsed.getSubject();
        } catch (SignatureException e) {
            throw new TokenExpiredException(e.getMessage());
        }
    }

    public String createAccessToken(String identity) {
        return _generateToken(identity, accessExp);
    }

    public String _generateToken(String identity, Long expSeconds) {
        return Jwts.builder()
                .setSubject(identity)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(expSeconds)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

}
