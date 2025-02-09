package com.ucan.helpdesk.service;

import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.enums.StatusTicket;
import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import com.ucan.helpdesk.model.*;
import com.ucan.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SLARepository slaRepository;

    @Autowired
    private UsuarioSuporteRepository usuarioSuporteRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public List<Ticket> listarTodos() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> buscarPorId(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> buscarPorStatus(String status) {
        return ticketRepository.findByStatus(status);
    }

    public List<Ticket> buscarPorPrioridade(Prioridade prioridade) {
        return ticketRepository.findByPrioridade(prioridade);
    }

    // Criar um novo ticket
    public Ticket createTicket(String descricao, String categoriaId, String impacto) {
        Categoria categoria = categoriaRepository.findById(Long.parseLong(categoriaId))
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Ticket ticket = new Ticket();
        ticket.setDescricao(descricao);
        ticket.setFkCategoria(categoria);
        ticket.setPrioridade(Prioridade.valueOf(categoria.getPrioridadePadrao().name()));
        ticket.setStatus(StatusTicket.ABERTO);
        ticket.setDataHoraCriacao(new Date());

        // Registrar log no Kafka
        kafkaTemplate.send("ticket-created", "Novo ticket criado: " + ticket.getDescricao());

        return ticketRepository.save(ticket);
    }

    // Triagem automática de tickets
    public void triagemAutomatica(Ticket ticket) {
        List<UsuarioSuporte> tecnicosDisponiveis = usuarioSuporteRepository.findTecnicosDisponiveis();

        for (UsuarioSuporte tecnico : tecnicosDisponiveis) {
            if (tecnico.getEspecialidades().contains(ticket.getFkCategoria().getNome())) {
                ticket.setFkTecnicoResponsavel(tecnico);
                ticket.setStatus(StatusTicket.EM_ANDAMENTO);

                // Registrar log no Kafka
                kafkaTemplate.send("ticket-updated", "Ticket atribuído ao técnico: " + tecnico.getNome());

                ticketRepository.save(ticket);
                break;
            }
        }
    }

    // Escalonamento de tickets
    public void escalonarTicket(Ticket ticket) {
        if (StatusTicket.ESCALONADO.equals(ticket.getStatus())) {
            throw new RuntimeException("Ticket já está escalonado");
        }

        Optional<UsuarioSuporte> supervisor = usuarioSuporteRepository.findByTipo(TipoUsuarioSuporte.SUPERVISOR).stream().findFirst();

        if (supervisor.isPresent()) {
            ticket.setFkTecnicoResponsavel(supervisor.get());
            ticket.setStatus(StatusTicket.ESCALONADO);

            // Registrar log no Kafka
            kafkaTemplate.send("ticket-escalated", "Ticket escalonado para o supervisor: " + supervisor.get().getNome());

            ticketRepository.save(ticket);
        } else {
            throw new RuntimeException("Nenhum supervisor disponível");
        }
    }

    // Fechamento de ticket
    public Ticket fecharTicket(Long ticketId, String feedback) {
        return ticketRepository.findById(ticketId)
                .map(ticket -> {
                    ticket.setStatus(StatusTicket.RESOLVIDO);
                    ticket.setFeedback(feedback);

                    // Registrar log no Kafka
                    kafkaTemplate.send("ticket-closed", "Ticket fechado: " + ticket.getDescricao());

                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
    }

    public void excluir(Long id) {
        ticketRepository.deleteById(id);
    }
}