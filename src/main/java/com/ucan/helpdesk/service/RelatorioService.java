package com.ucan.helpdesk.service;

import com.ucan.helpdesk.enums.StatusTicket;
import com.ucan.helpdesk.model.Relatorio;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SLAService slaService;

    public Relatorio gerarRelatorio(String periodo) {
        List<Ticket> tickets = ticketRepository.findAll();

        int totalTickets = tickets.size();
        int ticketsDentroSLA = 0;
        long tempoTotalResolucao = 0;

        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == StatusTicket.RESOLVIDO && !slaService.SLAViolado(ticket)) {
                ticketsDentroSLA++;
            }

            if (ticket.getStatus() == StatusTicket.RESOLVIDO) {
                Duration duracao = Duration.between(ticket.getDataHoraCriacao().toInstant(), LocalDateTime.now());
                tempoTotalResolucao += duracao.toMinutes();
            }
        }

        double taxaConformidadeSLA = (double) ticketsDentroSLA / totalTickets * 100;
        double tempoMedioResolucao = tempoTotalResolucao / totalTickets;

        return new Relatorio(periodo, taxaConformidadeSLA, tempoMedioResolucao);
    }
}