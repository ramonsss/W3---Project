package com.example.safecard.adapter.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
}
