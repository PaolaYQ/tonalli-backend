package com.example.tonalli_backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tonalli_backend.dto.AuthResponse;
import com.example.tonalli_backend.dto.LoginRequest;
import com.example.tonalli_backend.dto.RegisterRequest;
import com.example.tonalli_backend.entity.Alumno;
import com.example.tonalli_backend.entity.Maestro;
import com.example.tonalli_backend.entity.Usuario;
import com.example.tonalli_backend.repository.AlumnoRepository;
import com.example.tonalli_backend.repository.MaestroRepository;
import com.example.tonalli_backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MaestroRepository maestroRepository;
    private final AlumnoRepository alumnoRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), 
                        request.getPassword()));

        Usuario user = userRepository.findByCorreo(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado después de la autenticación"));

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String role;

        if ("A".equalsIgnoreCase(request.getRol())) {
            role = "ROLE_ALUMNO";
        } else if ("M".equalsIgnoreCase(request.getRol())) {
            role = "ROLE_MAESTRO";
        } else {
            throw new IllegalArgumentException("Rol no válido" + request.getRol());
        }

        Usuario user = Usuario.builder()
                .correo(request.getUsername())
                .contrasena(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getName())
                .rol(role)
                .build();

        Usuario savedUser = userRepository.save(user);
        if (role.equals("ROLE_MAESTRO")) {
            Maestro maestro = Maestro.builder()

                    .usuario(savedUser).build();
            maestroRepository.save(maestro);
        }
        if (role.equals("ROLE_ALUMNO")) {
            Alumno alumno = Alumno.builder()

                    .estrellasDisponibles(0)
                    .estrellasTotales(0)
                    .usuario(savedUser)
                    .build();
            alumnoRepository.save(alumno);
        }

        return AuthResponse.builder().token(jwtService.getToken(user)).build();

    }

}
