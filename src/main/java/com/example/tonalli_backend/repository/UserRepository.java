package com.example.tonalli_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tonalli_backend.entity.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String email);

}
