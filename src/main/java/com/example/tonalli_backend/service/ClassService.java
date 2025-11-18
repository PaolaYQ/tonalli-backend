package com.example.tonalli_backend.service;

import com.example.tonalli_backend.dto.CreateClassRequest;
import com.example.tonalli_backend.dto.CreateClassResponse;
import com.example.tonalli_backend.entity.Clase;
import com.example.tonalli_backend.entity.Maestro;
import com.example.tonalli_backend.entity.Usuario;
import com.example.tonalli_backend.repository.ClaseRepository;
import com.example.tonalli_backend.repository.MaestroRepository;
import com.example.tonalli_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClaseRepository claseRepository;
    private final UserRepository userRepository;
    private final MaestroRepository maestroRepository;

    // Caracteres permitidos para el código (evitamos O, 0, I, 1 para no confundir)
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public CreateClassResponse createClass(String teacherEmail, CreateClassRequest request) {
        
        // 1. Validar Grado (1 al 6)
        if (request.getGrado() < 1 || request.getGrado() > 6) {
            throw new IllegalArgumentException("El grado debe estar entre 1 y 6.");
        }

        // 2. Obtener al Maestro usando el correo del token
        Usuario user = userRepository.findByCorreo(teacherEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Maestro maestro = maestroRepository.findById(user.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("El usuario no es un maestro registrado"));

        // 3. Generar Código Único
        String uniqueCode = generateUniqueCode();

        // 4. Crear y Guardar la Clase
        Clase nuevaClase = Clase.builder()
                .nombreClase(request.getNombreClase())
                .grado(request.getGrado())
                .codigoClase(uniqueCode)
                .maestro(maestro) // Relación con el maestro
                .build();

        claseRepository.save(nuevaClase);

        // 5. Retornar respuesta
        return CreateClassResponse.builder()
                .codigoClase(uniqueCode)
                .build();
    }

    // --- Métodos Auxiliares ---

    private String generateUniqueCode() {
        String code;
        do {
            code = generateRandomString();
        } while (claseRepository.existsByCodigoClase(code)); // Verifica que no exista en la BD
        return code;
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}