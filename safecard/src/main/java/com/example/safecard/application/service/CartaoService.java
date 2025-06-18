package com.example.safecard.application.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.safecard.domain.model.Cartao;
import com.example.safecard.domain.model.Usuario;
import com.example.safecard.domain.model.enums.CartaoBandeira;
import com.example.safecard.domain.model.enums.CartaoMotivoBloqueio;
import com.example.safecard.domain.model.enums.CartaoStatus;
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

    
    public Cartao solicitarCartao(String cpf, String tipoCartao, CartaoBandeira bandeira) {
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
        cartao.setStatus(CartaoStatus.SOLICITADO);
        cartao.setStatus(CartaoStatus.SOLICITADO);
        cartao.setDataSolicitacao(LocalDate.now());

        // salva no banco
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> listarCartoes() {
        return cartaoRepository.findAll();
    }

    public void excluirCartao(Long id) {
        cartaoRepository.deleteById(id);
    }

    public Cartao pesquisarCartaoPeloNumero(String numeroCartao) {
        String numeroCartaoLimpo = numeroCartao.replaceAll("[^\\d]", "");
        List<Cartao> cartoes = cartaoRepository.findAll();
        for (Cartao c : cartoes) {
            String cNumeroLimpo = c.getNumeroCartao().replaceAll("[^\\d]", "");
            if (cNumeroLimpo.equals(numeroCartaoLimpo)) {
                return c;
            }
        }
        return null;
    }
    
    private boolean validarCpf(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", "");

        if(cpf.length() != 11) {
            return false;
        }

        // para verificar se o cpf não possui o mesmo número em tudo
        if(cpf.chars().distinct().count() == 1) {
            return false;
        }

        try {
            int soma = 0;
            for(int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int  digito1 =  11 - (soma %  11);
            if(digito1 >= 10) digito1 = 0;

            soma = 0;
            for(int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int digito2 = 11 - (soma % 11);
            if(digito2 >= 10) digito2 = 0;

            return digito1 == (cpf.charAt(9) - '0') && digito2 == (cpf.charAt(10) - '0');
        } catch (NumberFormatException e) {
            return false;
        }
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
        return String.format("%04d%04d%04d%04d",
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000));
    }

    public String aprovarCartao(String numeroCartao) {
        String numeroCartaoLimpo = numeroCartao.replaceAll("[^\\d]", "");

        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartaoLimpo);

        if(cartao == null) {
            throw new IllegalArgumentException("Cartão não encontrado");
        }

        if(cartao.getStatus() !=  CartaoStatus.SOLICITADO) {
            throw new IllegalStateException("Somente cartão com status de 'SOLICITADO' podem ser aprovados");
        }

        
        String senhaInicial = gerarSenhaInicial();
        cartao.setStatus(CartaoStatus.APROVADO);
        cartao.setSenhaCadastrada(true);
        cartao.setSenhaHash(senhaInicial);

        cartaoRepository.save(cartao);

        return senhaInicial;
    }

    public Cartao ativarCartao(String numeroCartao, String cpf, String senhaInformada) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao.replaceAll("[^\\d]", ""));

        if(cartao == null) {
            throw new IllegalArgumentException("Cartão não encontrado");
        }

        if(!cartao.getUsuario().getCpf().equals(cpf)) {
            throw new IllegalArgumentException("CPF não corresponde ao titular do cartão");
        }

        if(!(cartao.getStatus() == CartaoStatus.APROVADO || cartao.getStatus() == CartaoStatus.ENTREGUE)) {
            throw new IllegalStateException("Cartão não está em um status válido para ativação");
        }

        if(!cartao.getSenhaHash().equals(senhaInformada)) {
            throw new IllegalArgumentException("Senha incorreta!");
        }

        cartao.setStatus(CartaoStatus.ATIVO);
        return cartaoRepository.save(cartao);
    }

    public String gerarSenhaInicial() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public void bloqueioTemporario(String numeroCartao, String cpf, CartaoMotivoBloqueio bloqueioMotivo) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (cartao == null) {
            throw new IllegalArgumentException("Cartão não encontrado");
        }
        if (!cartao.getUsuario().getCpf().equals(cpf)) {
            throw new IllegalArgumentException("CPF não corresponde ao titular do cartão");
        }
        if (bloqueioMotivo == null) {
            throw new IllegalArgumentException("Motivo do bloqueio deve ser informado");
        }
        cartao.setStatus(CartaoStatus.BLOQUEADO_TEMPORARIO);
        cartao.setMotivoBloqueio(bloqueioMotivo);
        cartaoRepository.save(cartao);

    }

    public void cancelamentoDefinitivo(String numeroCartao, String cpf, CartaoMotivoBloqueio bloqueioMotivo) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (cartao == null) {
            throw new IllegalArgumentException("Cartão não encontrado");
        }
        if (!cartao.getUsuario().getCpf().equals(cpf)) {
            throw new IllegalArgumentException("CPF não corresponde ao titular do cartão");
        }
        if (bloqueioMotivo == null) {
            throw new IllegalArgumentException("Motivo do cancelamento deve ser informado");
        }
        if (cartao.getStatus() == CartaoStatus.CANCELADO) {
            throw new IllegalStateException("Cartão já está cancelado");
        }
        if (cartao.isPossuiFaturaAberta()) {
            throw new IllegalArgumentException("Cartão não pode ser cancelado por possuir fatura em aberto");
        }
        cartao.setStatus(CartaoStatus.CANCELADO);
        cartao.setMotivoBloqueio(bloqueioMotivo);
        cartaoRepository.save(cartao);
    }
}
