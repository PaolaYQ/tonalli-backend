package com.example.tonalli_backend.service;

import com.example.tonalli_backend.dto.*;
import com.example.tonalli_backend.entity.*;
import com.example.tonalli_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final UserRepository userRepository;
    private final AlumnoRepository alumnoRepository;
    private final TiendaCosmeticoRepository tiendaRepository;
    private final AlumnoItemRepository alumnoItemRepository;

    // --- 1. OBTENER TIENDA ---
    @Transactional(readOnly = true)
    public ShopResponse getShopData(String email) {
        Usuario user = userRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Alumno alumno = user.getAlumno();

        // Obtener todo el catálogo y lo que ya tiene el alumno
        List<TiendaCosmetico> catalogo = tiendaRepository.findAll();
        List<AlumnoItem> misItems = alumnoItemRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());

        // Sets para búsqueda rápida (O(1))
        Set<Integer> idsAdquiridos = misItems.stream().map(ai -> ai.getItem().getIdItem()).collect(Collectors.toSet());
        Set<Integer> idsEnUso = misItems.stream().filter(AlumnoItem::getEnUso).map(ai -> ai.getItem().getIdItem())
                .collect(Collectors.toSet());

        // Agrupar por Tipo (ej. "Sombrero", "Ropa")
        Map<String, List<ShopItemDTO>> agrupados = catalogo.stream()
                .map(item -> {
                    boolean adquirido = idsAdquiridos.contains(item.getIdItem());
                    boolean enUso = idsEnUso.contains(item.getIdItem());

                    return new AbstractMap.SimpleEntry<>(
                            item.getTipoCosmetico().getNombreTipo(), // Key
                            ShopItemDTO.builder() // Value
                                    .idItem(item.getIdItem())
                                    .nombre(item.getNombre())
                                    .precio(item.getPrecioEstrellas())
                                    .iconoUrl(item.getIconoUrl())
                                    .adquirido(adquirido)
                                    .enUso(enUso)
                                    .build());
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        return ShopResponse.builder()
                .estrellasDisponibles(alumno.getEstrellasDisponibles())
                .itemsPorTipo(agrupados)
                .build();
    }

    // --- 2. COMPRAR ---
    @Transactional
    public void buyItem(String email, Integer idItem) {
        Usuario user = userRepository.findByCorreo(email).orElseThrow();
        Alumno alumno = user.getAlumno();

        TiendaCosmetico item = tiendaRepository.findById(idItem)
                .orElseThrow(() -> new IllegalArgumentException("Ítem no encontrado"));

        if (alumnoItemRepository.existsByAlumno_IdAlumnoAndItem_IdItem(alumno.getIdAlumno(), idItem)) {
            throw new IllegalStateException("Ya tienes este ítem.");
        }
        if (alumno.getEstrellasDisponibles() < item.getPrecioEstrellas()) {
            throw new IllegalStateException("No tienes suficientes estrellas.");
        }

        // Descontar saldo
        alumno.setEstrellasDisponibles(alumno.getEstrellasDisponibles() - item.getPrecioEstrellas());
        alumnoRepository.save(alumno);

        // Registrar compra
        AlumnoItem nuevoItem = AlumnoItem.builder()
                .alumno(alumno)
                .item(item)
                .enUso(false)
                .build();
        alumnoItemRepository.save(nuevoItem);
    }

    // --- 3. GUARDAR AVATAR (Actualizar perfil + Items en uso) ---
    @Transactional
    public void saveAvatarConfiguration(String email, AvatarUpdateRequest request) {
        Usuario user = userRepository.findByCorreo(email).orElseThrow();
        Alumno alumno = user.getAlumno();

        // A. Actualizar inventario (qué trae puesto)
        List<AlumnoItem> misItems = alumnoItemRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());

        for (AlumnoItem ai : misItems) {
            boolean equipar = request.getItemsEquipadosIds().contains(ai.getItem().getIdItem());
            ai.setEnUso(equipar);
        }
        alumnoItemRepository.saveAll(misItems);

        // B. Guardar la foto generada en BD
        if (request.getImagenBase64() != null && request.getImagenBase64().contains(",")) {
            // Limpiar el prefijo "data:image/png;base64,"
            String base64Clean = request.getImagenBase64().split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64Clean);

            user.setAvatarBits(imageBytes);
            userRepository.save(user);
        }
    }
}