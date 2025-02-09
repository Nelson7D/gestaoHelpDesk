package com.ucan.helpdesk.service;

import com.ucan.helpdesk.model.Colaborador;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.ColaboradorRepository;
import com.ucan.helpdesk.repository.TicketRepository;
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

    public List<Colaborador> listarTodos() {
        return colaboradorRepository.findAll();
    }

    public Optional<Colaborador> buscarPorId(Long id) {
        return colaboradorRepository.findById(id);
    }

    public List<Colaborador> buscarPorSetor(String setor) {
        return colaboradorRepository.findBySetor(setor);
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
