package com.ucan.helpdesk.service;

import com.ucan.helpdesk.enums.StatusUsuario;
import com.ucan.helpdesk.model.Colaborador;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.ColaboradorRepository;
import com.ucan.helpdesk.repository.TicketRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Colaborador> listarTodos() {
        return colaboradorRepository.findAll();
    }

    public Colaborador buscarPorEmail(String email) {
        return colaboradorRepository.findByEmail(email);
    }

    public Optional<Colaborador> buscarPorId(Long id) {
        return colaboradorRepository.findById(id);
    }

    public List<Colaborador> buscarPorSetor(String setor) {
        return colaboradorRepository.findBySetor(setor);
    }

    public List<Colaborador> buscarColaboradoresPorEmailEStatus(String email, String status) {
        try {
            StatusUsuario statusUsuario = StatusUsuario.valueOf(status.toUpperCase());
            return colaboradorRepository.findByEmailAndStatusUsuario(email, statusUsuario);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + status);
        }
    }

    public Colaborador criarColaborador(Colaborador colaborador) {
        colaborador.setSenha(passwordEncoder.encode(colaborador.getSenha()));
        return colaboradorRepository.save(colaborador);
    }

    public Colaborador atualizarColaborador(Long id, Colaborador colaboradorDados) {
        return colaboradorRepository.findById(id)
                .map(colaborador -> {
                    colaborador.setNome(colaboradorDados.getNome());
                    colaborador.setEmail(colaboradorDados.getEmail());
                    colaborador.setSenha(passwordEncoder.encode(colaboradorDados.getSenha()));
                    colaborador.setStatusUsuario(colaboradorDados.getStatusUsuario());
                    return colaboradorRepository.save(colaborador);
                })
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
    }

    public Colaborador salvar(Colaborador colaborador) {
        return colaboradorRepository.save(colaborador);
    }

    public Ticket criarTicketParaColaborador(Long colaboradorId, Ticket ticket) {
        return colaboradorRepository.findById(colaboradorId)
                .map(colaborador -> {
                    ticket.setFkColaborador(colaborador);
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
    }

    public void excluir(Long id) {
        colaboradorRepository.deleteById(id);
    }
}
