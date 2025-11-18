package com.example.tonalli_backend.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tonalli_backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // DEBUG 1: Verificamos si llega el header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ FILTRO JWT: No se encontró header Authorization o no empieza con Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        System.out
                .println("🔍 FILTRO JWT: Token recibido (inicio): " + jwt.substring(0, min(jwt.length(), 15)) + "...");

        try {
            // DEBUG 2: Intentamos extraer el usuario
            userEmail = jwtService.extractUsername(jwt);
            System.out.println("✅ FILTRO JWT: Usuario extraído del token: " + userEmail);
        } catch (Exception e) {
            // AQUÍ ES DONDE PROBABLEMENTE ESTÁ FALLANDO
            System.out.println(
                    "❌ FILTRO JWT: Error CRÍTICO al leer el token (Firma inválida o expirado): " + e.getMessage());
            e.printStackTrace(); // Imprime el error completo para verlo
            filterChain.doFilter(request, response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("🔄 FILTRO JWT: Buscando usuario en BD...");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("✅ FILTRO JWT: Token válido. Autenticando...");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("❌ FILTRO JWT: Token inválido según jwtService.");
            }
        }

        filterChain.doFilter(request, response);
    }

    private int min(int a, int b) {
        return Math.min(a, b);
    }
}