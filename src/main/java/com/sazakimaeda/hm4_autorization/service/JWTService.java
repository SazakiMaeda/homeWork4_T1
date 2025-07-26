package com.sazakimaeda.hm4_autorization.service;

import com.sazakimaeda.hm4_autorization.dto.JWTAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class JWTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    @Value("${jwt.service.jwtsecret}")
    private String jwtSecret;

    public boolean validateJWTToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException e){
            LOGGER.error("Expired JWTException", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JWTException", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Malformed JWTException", e);
        } catch (SecurityException e) {
            LOGGER.error("Security Exception", e);
        } catch (Exception e){
            LOGGER.error("Invalid token", e);
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public JWTAuthenticationDto generateAuthenticationToken(String email) {
        JWTAuthenticationDto jwtDto = new JWTAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(generateRefreshToken(email));
        return jwtDto;
    }

    public JWTAuthenticationDto refreshBaseToken(String email, String refreshToken) {
        JWTAuthenticationDto jwtDto = new JWTAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    private String generateJwtToken(String email) {
        Date date = Date.from(
                LocalDateTime.now()
                        .plusMinutes(1)
                        .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSecretKey())
                .compact();
    }

    private String generateRefreshToken(String email) {
        Date date = Date.from(
                LocalDateTime.now()
                        .plusDays(1)
                        .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(secretBytes);
    }

}
