package com.ucan.helpdesk.service;

import com.ucan.helpdesk.enums.StatusUsuario;
import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.model.UsuarioSuporte;
import com.ucan.helpdesk.repository.TicketRepository;
import com.ucan.helpdesk.repository.UsuarioSuporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioSuporteService {

    @Autowired
    private UsuarioSuporteRepository usuarioSuporteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioSuporte> listarTodos() {
        return usuarioSuporteRepository.findAll();
    }

    public Optional<UsuarioSuporte> buscarPorId(Long id) {
        return usuarioSuporteRepository.findById(id);
    }

    public List<UsuarioSuporte> buscarPorTipo(TipoUsuarioSuporte tipo) {
        return usuarioSuporteRepository.findByTipo(tipo);
    }

    public UsuarioSuporte buscarPorEmail(String email) {
        return usuarioSuporteRepository.findByEmail(email);
    }

    public List<UsuarioSuporte> buscarUsuariosSuportePorEmailEStatus(String email, String status) {
        try {
            StatusUsuario statusUsuario = StatusUsuario.valueOf(status.toUpperCase());
            return usuarioSuporteRepository.findByEmailAndStatusUsuario(email, statusUsuario);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + status);
        }
    }

    public List<UsuarioSuporte> buscarPorEspecialidade(String especialidade) {
        return usuarioSuporteRepository.findByEspecialidadesContaining(especialidade);
    }

    public List<UsuarioSuporte> findByTipo(TipoUsuarioSuporte tipo) {
        return usuarioSuporteRepository.findByTipo(tipo);
    }

    public boolean temEspecialidade(Long tecnicoId, String especialidade) {
        return usuarioSuporteRepository.findById(tecnicoId)
                .filter(tecnico -> tecnico.getEspecialidades().contains(especialidade))
                .isPresent();
    }

    public UsuarioSuporte criarUsuarioSuporte(UsuarioSuporte usuarioSuporte) {
        usuarioSuporte.setSenha(passwordEncoder.encode(usuarioSuporte.getSenha()));
        return usuarioSuporteRepository.save(usuarioSuporte);
    }

    public UsuarioSuporte atualizarUsuarioSuporte(Long id, UsuarioSuporte usuarioDados) {
        return usuarioSuporteRepository.findById(id)
                .map(usuarioSuporte -> {
                    usuarioSuporte.setNome(usuarioDados.getNome());
                    usuarioSuporte.setEmail(usuarioDados.getEmail());
                    usuarioSuporte.setSenha(passwordEncoder.encode(usuarioDados.getSenha()));
                    usuarioSuporte.setStatusUsuario(usuarioDados.getStatusUsuario());
                    return usuarioSuporteRepository.save(usuarioSuporte);
                })
                .orElseThrow(() -> new RuntimeException("Usuário de Suporte não encontrado"));
    }

    public Ticket atribuirTicketpPraTecnico(Long ticketId, Long tecnicoId) {
        return usuarioSuporteRepository.findById(tecnicoId)
                .map(tecnico -> ticketRepository.findById(ticketId)
                        .map(ticket -> {
                            ticket.setFkTecnicoResponsavel(tecnico);
                            return ticketRepository.save(ticket);
                        })
                        .orElseThrow(() -> new RuntimeException("Ticket não encontrado")))
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
    }

    public UsuarioSuporte salvar(UsuarioSuporte usuarioSuporte) {
        return usuarioSuporteRepository.save(usuarioSuporte);
    }

    public void excluir(Long id) {
        usuarioSuporteRepository.deleteById(id);
    }
}