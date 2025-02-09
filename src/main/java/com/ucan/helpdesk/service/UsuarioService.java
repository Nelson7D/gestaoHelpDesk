package com.ucan.helpdesk.service;

import com.ucan.helpdesk.model.Usuario;
import com.ucan.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Buscar usuário ativo por email
    public Optional<Usuario> buscarPorEmailEStatus(String email, String status) {
        return usuarioRepository.findByEmailAndStatus(email, status);
    }
    // Salvar / Criar usúario
    public Usuario criarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            usuario.getRoles().add("COLABORADOR");
        }
        return usuarioRepository.save(usuario);
    }

    // Atualizar informações de um usuário
    public Usuario atualizarUsuario(Long id, Usuario usuarioDados) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioDados.getNome());
                    usuario.setEmail(usuarioDados.getEmail());
                    usuario.setSenha(usuarioDados.getSenha());
                    usuario.setStatus(usuarioDados.getStatus());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }



}
