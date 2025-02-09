package com.ucan.helpdesk.service;

import com.ucan.helpdesk.model.SLA;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.SLARepository;
import com.ucan.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SLAService {

    @Autowired
    private SLARepository slaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public List<SLA> listarTodos() {
        return slaRepository.findAll();
    }

    public Optional<SLA> buscarPorId(Long id) {
        return slaRepository.findById(id);
    }

    public List<SLA> buscarPorTempoMaximoResposta(Integer tempo) {
        return slaRepository.findByTempoMaximoRespostaLessThanEqual(tempo);
    }

    public List<SLA> buscarPorTempoMaximoResolucao(Integer tempo) {
        return slaRepository.findByTempoMaximoResolucaoLessThanEqual(tempo);
    }

    public SLA salvar(SLA sla) {
        return slaRepository.save(sla);
    }

    public boolean SLAViolado(Ticket ticket) {
        SLA sla = slaRepository.findByFkCategoria(ticket.getFkCategoria())
                .orElseThrow(() -> new RuntimeException("SLA não encontrado para a categoria"));

        long tempoDecorrido = ChronoUnit.MINUTES.between((Temporal) ticket.getDataHoraCriacao(), (Temporal) new Date());
        return tempoDecorrido > sla.getTempoMaximoResposta() || tempoDecorrido > sla.getTempoMaximoResolucao();
    }

    public void gerarAlertaSLA() {
        List<Ticket> ticketsViolados = ticketRepository.findTicketsComSLAViolado();
        for (Ticket ticket : ticketsViolados) {
            kafkaTemplate.send("sla-violated", "SLA violado para o ticket: " + ticket.getDescricao());
        }
    }
}