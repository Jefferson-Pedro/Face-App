package com.face_app.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.face_app.project.model.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

public class TokenUtil {

    public static final long EXPIRATION = 7 * 24 * 60 * 1000;
    public static final String  ISSUER = "FaceApp";
    public static final String  SECRET_KEY = "01234567890123456789012345678901";
    public static final String PREFIX = "Bearer ";

    public static FaceToken encode (User user){

        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // Chave de criptogragia.
            String jwtToken = Jwts.builder().subject(user.getNome())
                    .issuer(ISSUER)
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                    .signWith(key)
                    .compact();

            return new FaceToken(PREFIX + jwtToken);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o token: " + e);
        }
    }

    public static Authentication decode(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null){
                token = token.replace(PREFIX, "");
                SecretKey secretKey = Keys.hmacShaKeyFor((SECRET_KEY.getBytes()));

                JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();

                Claims claims = (Claims) jwtParser.parse(token).getPayload();
                String issuer = claims.getIssuer();
                String subject = claims.getSubject();
                Date expiration = claims.getExpiration();

                if(isValid(issuer, subject, expiration)){
                    return new UsernamePasswordAuthenticationToken(subject , null, Collections.emptyList());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decodificar o token: " + e);
        }
        return null;
    }

    private static boolean isValid(String issuer, String subject, Date expiration) {
        return issuer.equals(ISSUER) && subject != null && !subject.isEmpty() && expiration.after(new Date(System.currentTimeMillis()));
    }
}
