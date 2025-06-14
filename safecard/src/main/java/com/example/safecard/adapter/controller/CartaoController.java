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
import org.springframework.web.bind.annotation.RequestParam;
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


    @GetMapping
    public List<Cartao> listarCartoes() {
        return cartaoService.listarCartoes();
    }

    @GetMapping("/{numero_cartao}")
    public Cartao buscaCartaoPeloNumero(@PathVariable String numeroCartao) {
        return cartaoService.pesquisarCartaoPeloNumero(numeroCartao);
    }

    @DeleteMapping("/{id}")
    public void excluirUsuario(@PathVariable Long id) {
        cartaoService.excluirCartao(id);
    }

    @PatchMapping("/aprovar")
    public ResponseEntity<String> aprovarCartao(@RequestParam String numeroCartao) {
        String senha = cartaoService.aprovarCartao(numeroCartao);
        return ResponseEntity.ok(senha);
    }

    @PostMapping("/ativar")
    public ResponseEntity<Cartao> ativarCartao(@RequestBody CartaoRequestDTO dto) {
        Cartao cartao = cartaoService.ativarCartao(dto.getNumeroCartao(), dto.getCpf(), dto.getSenha());
        return ResponseEntity.ok(cartao);
    }
}
