package com.face_app.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class MyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

       /* System.out.println("URI: " + request.getRequestURI());
        System.out.println("Método: " + request.getMethod());
        System.out.println("Authorization Header: " + request.getHeader("Authorization"));*/

        String authHeader = request.getHeader("Authorization");

        // Se não tem token, deixa o Spring Security decidir se a rota é pública ou não
        if (authHeader == null || authHeader.isEmpty()) {
            //System.out.println("Sem token - delegando para Spring Security");
            filterChain.doFilter(request, response);
            return;
        }

        // Se tem token, tenta validar
        Authentication auth = TokenUtil.decode(request);
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
            return;
        }

        // Token presente mas inválido -> 401
        System.err.println("Token inválido!");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().println("{\"erro\": \"Token inválido ou expirado\"}");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldSkip = path.startsWith("/h2-console");

        // Não aplica o filtro para o console H2
        return shouldSkip;
    }
}