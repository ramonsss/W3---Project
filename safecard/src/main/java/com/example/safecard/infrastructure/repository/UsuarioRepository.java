package com.example.safecard.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.safecard.domain.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByCpf(String cpf);
}
