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
