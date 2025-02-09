package com.ucan.helpdesk.service;

import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.model.UsuarioSuporte;
import com.ucan.helpdesk.repository.TicketRepository;
import com.ucan.helpdesk.repository.UsuarioSuporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioSuporteService {

    @Autowired
    private UsuarioSuporteRepository usuarioSuporteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public List<UsuarioSuporte> listarTodos() {
        return usuarioSuporteRepository.findAll();
    }

    public Optional<UsuarioSuporte> buscarPorId(Long id) {
        return usuarioSuporteRepository.findById(id);
    }

    public List<UsuarioSuporte> buscarPorTipo(TipoUsuarioSuporte tipo) {
        return usuarioSuporteRepository.findByTipo(tipo);
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