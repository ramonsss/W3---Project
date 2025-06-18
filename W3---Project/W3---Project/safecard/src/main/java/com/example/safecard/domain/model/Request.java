package com.example.safecard.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String justificativa;
    private String limite;
    private double valor;
    private String cpf;
    private String numeroCartao;
    //para consegui acessar as quisicoes do usuario
    private String numero_da_requisicao;


    public Request(Long id, String justificativa, String limite, double valor, String cpf, String numeroCartao) {
        this.id = id;
        this.justificativa = justificativa;
        this.limite = limite;
        this.valor = valor;
        this.cpf = cpf;
        this.numeroCartao = numeroCartao;
    }

    public Request() {
    }

    public Long getId() {
        return id;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public String getLimite() {
        return limite;
    }

    public double getValor() {
        return valor;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public void setLimite(String limite) {
        this.limite = limite;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getNumero_da_requisicao() {
        return numero_da_requisicao;
    }

    public void setNumero_da_requisicao(String numero_da_requisiscao) {
        this.numero_da_requisicao = numero_da_requisiscao;
    }


}
