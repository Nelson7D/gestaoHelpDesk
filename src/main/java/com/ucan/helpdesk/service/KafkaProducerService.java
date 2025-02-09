package com.ucan.helpdesk.service;

import com.ucan.helpdesk.model.Ticket;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String,Ticket> ticketKafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, KafkaTemplate<String, Ticket> ticketKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.ticketKafkaTemplate = ticketKafkaTemplate;
    }

    public void enviarTicketCriado(Ticket ticket) {
        ticketKafkaTemplate.send("ticket-criado", ticket);
    }

    public void enviarTicketAtualizado(String mensagem) {
        kafkaTemplate.send("ticket-atualizado", mensagem);
    }

    public void enviarTicketFechado(String mensagem) {
        kafkaTemplate.send("ticket-fechado", mensagem);
    }

    public void enviarSLAViolado(String mensagem) {
        kafkaTemplate.send("sla-violado", mensagem);
    }

    public void enviarEventoLog(String mensagem) {
        kafkaTemplate.send("log-evento", mensagem);
    }
}