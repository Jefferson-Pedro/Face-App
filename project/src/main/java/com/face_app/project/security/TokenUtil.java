package com.face_app.project.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

public class TokenUtil {

    public static Authentication decode(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        System.out.println("Recebida requisição com cabeçalho = " + authHeader);

        // Validação de segurança
        if (authHeader == null || authHeader.isEmpty()) {
            System.err.println("Cabeçalho de autorização ausente");
            return null;
        }

        if (authHeader.equals("Bearer 1234567890")) {
            System.out.println("Cabeçalho OK");
            return new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());
        }
        System.err.println("Token inválido: " + authHeader);
        return null;
    }
}