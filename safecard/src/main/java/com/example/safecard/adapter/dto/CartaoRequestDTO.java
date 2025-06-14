package com.example.safecard.adapter.dto;

import com.example.safecard.domain.model.enums.CartaoBandeira;
import com.example.safecard.domain.model.enums.CartaoMotivoBloqueio;

public class CartaoRequestDTO {
    private String numeroCartao;
    private String cpf;
    private String tipoCartao;
    private CartaoBandeira bandeira;
    private CartaoMotivoBloqueio motivoBloqueio;
    private String senha;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(String tipoCartao) {
        this.tipoCartao = tipoCartao;
    }

    public CartaoBandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(CartaoBandeira bandeira) {
        this.bandeira = bandeira;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public CartaoMotivoBloqueio getMotivoBloqueio() {
        return motivoBloqueio;
    }

    public void setMotivoBloqueio(CartaoMotivoBloqueio motivoBloqueio) {
        this.motivoBloqueio = motivoBloqueio;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
