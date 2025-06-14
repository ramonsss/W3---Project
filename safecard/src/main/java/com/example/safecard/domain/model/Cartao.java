package com.example.safecard.domain.model;
import java.time.LocalDate;

import com.example.safecard.domain.model.enums.CartaoBandeira;
import com.example.safecard.domain.model.enums.CartaoMotivoBloqueio;
import com.example.safecard.domain.model.enums.CartaoStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCartao;
    private CartaoBandeira bandeira; // Visa, Mastercard, Elo
    private String tipo; // credito ou debito
    private CartaoStatus status;  // solicitado, ativo, bloqueado_temporario, etc...
    private LocalDate dataSolicitacao;
    private LocalDate dataAtivacao;

    private double limite;
    private CartaoMotivoBloqueio motivoBloqueio;
    private String motivoCancelamento;

    private String senhaHash;
    private boolean senhaCadastrada;

    private boolean possuiFaturaAberta;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Cartao() {
    }

    public Cartao(String numeroCartao, CartaoBandeira bandeira, String tipo, CartaoStatus status, LocalDate dataSolicitacao,
            Usuario usuario) {
        this.numeroCartao = numeroCartao;
        this.bandeira = bandeira;
        this.tipo = tipo;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
        this.usuario = usuario;
        this.senhaCadastrada = false;
        this.possuiFaturaAberta = false;
    }



    // metodos gerais
    

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public CartaoBandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(CartaoBandeira bandeira) {
        this.bandeira = bandeira;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public CartaoStatus getStatus() {
        return status;
    }

    public void setStatus(CartaoStatus status) {
        this.status = status;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDate getDataAtivacao() {
        return dataAtivacao;
    }

    public void setDataAtivacao(LocalDate dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public CartaoMotivoBloqueio getMotivoBloqueio() {
        return motivoBloqueio;
    }

    public void setMotivoBloqueio(CartaoMotivoBloqueio motivoBloqueio) {
        this.motivoBloqueio = motivoBloqueio;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public boolean isSenhaCadastrada() {
        return senhaCadastrada;
    }

    public void setSenhaCadastrada(boolean senhaCadastrada) {
        this.senhaCadastrada = senhaCadastrada;
    }

    public boolean isPossuiFaturaAberta() {
        return possuiFaturaAberta;
    }

    public void setPossuiFaturaAberta(boolean possuiFaturaAberta) {
        this.possuiFaturaAberta = possuiFaturaAberta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
