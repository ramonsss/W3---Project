package com.example.safecard.application;

import java.time.LocalDate;
import java.time.Period;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.safecard.domain.model.Cartao;
import com.example.safecard.domain.model.Usuario;
import com.example.safecard.infrastructure.repository.CartaoRepository;
import com.example.safecard.infrastructure.repository.UsuarioRepository;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;
    private final UsuarioRepository usuarioRepository;

    public CartaoService(CartaoRepository cartaoRepository, UsuarioRepository usuarioRepository) {
        this.cartaoRepository = cartaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    
    public Cartao solicitarCartao(String cpf, String tipoCartao, String bandeira) {
        // buscar usuario pelo cpf
        Usuario usuario = usuarioRepository.findByCpf(cpf);
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado para o CPF informado");
        }

        //validar dados do usuario
        if(!validarCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }

        if(!maiorDeIdade(usuario.getDataNascimento())) {
            throw new IllegalArgumentException("Usuário deve ser maior de 18 anos");
        }

        if(!validarRenda(usuario.getRendaMensal(), tipoCartao)) {
            throw new IllegalArgumentException("Renda insuficiente para o tipo de cartão");
        }

        // Cria cartao
        Cartao cartao = new Cartao();
        cartao.setUsuario(usuario);
        cartao.setNumeroCartao(gerarNumeroCartao());
        cartao.setTipo(tipoCartao);
        cartao.setBandeira(bandeira);
        cartao.setStatus("SOLICITADO");
        cartao.setDataSolicitacao(LocalDate.now());

        // salva no banco
        return cartaoRepository.save(cartao);
    }
    
    private boolean validarCpf(String cpf) {
        String regex = "\\d{11}";
        return Pattern.matches(regex, cpf);
    }
    
    private boolean maiorDeIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
    }
    
    private boolean validarRenda(double renda, String tipoCartao) {
        if ("credito".equalsIgnoreCase(tipoCartao)) {
            return renda >= 1500.0;
        } else if ("debito".equalsIgnoreCase(tipoCartao)) {
            return renda >= 500.0;
        }
        
        return false;
    }
    
    public String gerarNumeroCartao() {
        Random random = new Random();
        return String.format("%04d-%04d-%04d-%04d",
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000));
    }
}
