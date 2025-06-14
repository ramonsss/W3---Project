package com.example.safecard.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.safecard.domain.model.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Cartao findByNumeroCartao(String numeroCartao);
}
