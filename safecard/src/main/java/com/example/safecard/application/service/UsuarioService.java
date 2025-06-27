package com.example.safecard.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.safecard.domain.model.Usuario;
import com.example.safecard.infrastructure.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscaUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null); // se não tiver nenhum usuario cadastrado com esse id, ele vai retornar null
    }

    public Usuario buscaUsuarioPorCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
