package com.example.safecard.infrastructure.repository;

import com.example.safecard.domain.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findBynumero_da_requisicao(String numero_da_requisicao);
}
