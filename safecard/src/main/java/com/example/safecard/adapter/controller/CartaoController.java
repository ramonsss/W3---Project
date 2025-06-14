package com.example.safecard.adapter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.safecard.adapter.dto.CartaoRequestDTO;
import com.example.safecard.application.CartaoService;
import com.example.safecard.domain.model.Cartao;

@RestController
@RequestMapping("api/cartoes")
public class CartaoController {

    @Autowired
    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @PostMapping
    public Cartao solicitarCartao(@RequestBody CartaoRequestDTO dto) {
        return cartaoService.solicitarCartao(dto.getCpf(), dto.getTipoCartao(), dto.getBandeira());
    }

    @PatchMapping("/bloqueio")
    public ResponseEntity<String> bloqueioTemporario(@RequestBody CartaoRequestDTO dto) {
        try {
            cartaoService.bloqueioTemporario(dto.getNumeroCartao(), dto.getCpf(), dto.getMotivoBloqueio());
            return ResponseEntity.ok("Cartão bloqueado temporariamente com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Dados inválidos para bloqueio do cartão: " + e.getMessage());
        } catch (org.springframework.dao.DataAccessException e) {
            return ResponseEntity.status(500).body("Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro inesperado ao bloquear o cartão: " + e.getMessage());
        }
    }

}
