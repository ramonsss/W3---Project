package com.example.safecard.adapter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.safecard.adapter.dto.CartaoRequestDTO;
import com.example.safecard.application.service.CartaoService;
import com.example.safecard.domain.model.Cartao;

@RestController
@RequestMapping("/api/cartoes")
public class CartaoController {

    private final CartaoService cartaoService;

    @Autowired
    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    // Criar cartão
    @PostMapping
    public ResponseEntity<?> criarCartao(@RequestBody CartaoRequestDTO dto) {
        try {
            Cartao cartao = cartaoService.solicitarCartao(dto.getCpf(), dto.getTipoCartao(), dto.getBandeira());
            return ResponseEntity.ok(cartao);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao solicitar cartão: " + e.getMessage());
        }
    }

    // Listar cartões
    @GetMapping
    public ResponseEntity<List<Cartao>> listarCartoes() {
        try {
            return ResponseEntity.ok(cartaoService.listarCartoes());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Buscar cartão pelo número
    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> buscarCartaoPorNumero(@PathVariable String numeroCartao) {
        try {
            Cartao cartao = cartaoService.pesquisarCartaoPeloNumero(numeroCartao);
            if (cartao == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cartao);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar cartão: " + e.getMessage());
        }
    }

    // Excluir cartão
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirCartao(@PathVariable Long id) {
        try {
            cartaoService.excluirCartao(id);
            return ResponseEntity.ok("Cartão excluído com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir cartão: " + e.getMessage());
        }
    }

    // Bloqueio temporário
    @PatchMapping("/{numeroCartao}/bloqueio-temporario")
    public ResponseEntity<String> bloqueioTemporario(@PathVariable String numeroCartao, @RequestBody CartaoRequestDTO dto) {
        try {
            cartaoService.bloqueioTemporario(numeroCartao, dto.getCpf(), dto.getMotivoBloqueio());
            return ResponseEntity.ok("Cartão bloqueado temporariamente com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Dados inválidos para bloqueio do cartão: " + e.getMessage());
        } catch (org.springframework.dao.DataAccessException e) {
            return ResponseEntity.status(500).body("Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro inesperado ao bloquear o cartão: " + e.getMessage());
        }
    }

    // Aprovar cartão
    @PatchMapping("/{numeroCartao}/aprovar")
    public ResponseEntity<?> aprovarCartao(@PathVariable String numeroCartao) {
        try {
            String senha = cartaoService.aprovarCartao(numeroCartao);
            return ResponseEntity.ok(senha);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao aprovar cartão: " + e.getMessage());
        }
    }

    // Ativar cartão
    @PatchMapping("/{numeroCartao}/ativar")
    public ResponseEntity<?> ativarCartao(@PathVariable String numeroCartao, @RequestBody CartaoRequestDTO dto) {
        try {
            Cartao cartao = cartaoService.ativarCartao(numeroCartao, dto.getCpf(), dto.getSenha());
            return ResponseEntity.ok(cartao);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao ativar cartão: " + e.getMessage());
        }
    }
}
