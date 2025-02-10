package com.ucan.helpdesk.service;

import com.ucan.helpdesk.model.Log;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.LogRepository;
import com.ucan.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    LogRepository logRepository;

    @Autowired
    TicketService ticketService;

    @KafkaListener(topics = "ticket-criado", groupId = "helpdesk-group")
    public void listenTicketCreated(Ticket ticket) {
        ticketService.criarTicket(ticket);
        listenLogEvent("Ticket ID: "+ticket.getPkTicket()+"\nNovo ticket criado: " + ticket.getDescricao());
    }

    @KafkaListener(topics = "ticket-atualizado", groupId = "helpdesk-group")
    public void listenTicketUpdated(String mensagem) {
        System.out.println("Mensagem recebida no tópico ticket-updated: " + mensagem);
    }

    @KafkaListener(topics = "ticket-fechado", groupId = "helpdesk-group")
    public void listenTicketClosed(String mensagem) {
        System.out.println("Mensagem recebida no tópico ticket-closed: " + mensagem);
    }

    @KafkaListener(topics = "sla-violado", groupId = "helpdesk-group")
    public void listenSLAViolated(String mensagem) {
        System.out.println("Mensagem recebida no tópico sla-violated: " + mensagem);
    }

    @KafkaListener(topics = "log-evento", groupId = "helpdesk-group")
    public void listenLogEvent(String mensagem) {
        Log log = new Log();
        log.setMensagem(mensagem);
        logRepository.save(log);
    }

}