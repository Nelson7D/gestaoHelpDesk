package com.ucan.helpdesk.service;

import com.ucan.helpdesk.enums.EventoLog;
import com.ucan.helpdesk.model.Log;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.model.UsuarioSuporte;
import com.ucan.helpdesk.repository.LogRepository;
import com.ucan.helpdesk.repository.TicketRepository;
import com.ucan.helpdesk.repository.UsuarioSuporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import java.util.Optional;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioSuporteRepository usuarioSuporteRepository;

    public List<Log> listarTodos() {
        return logRepository.findAll();
    }

    public Optional<Log> buscarPorId(Long id) {
        return logRepository.findById(id);
    }

    public List<Log> buscarPorEvento(String evento) {
        try {
            EventoLog eventoLog = EventoLog.valueOf(evento.toUpperCase());
            return logRepository.findByEvento(eventoLog);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Evento inválido: " + evento);
        }
    }

    public List<Log> buscarPorTicket(Ticket ticket) {
        return logRepository.findByFkTicket(ticket);
    }

    public Optional<Ticket> buscarPorTicketId(Long fkTicket) {
        return ticketRepository.findById(fkTicket);
    }

    public Log salvar(Log log) {
        return logRepository.save(log);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Registrar um log para um ticket
    public void registerLog(Long ticketId, String mensagem, EventoLog evento, Long tecnicoId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));

        UsuarioSuporte tecnico = null;
        if (tecnicoId != null) {
            tecnico = usuarioSuporteRepository.findById(tecnicoId)
                    .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
        }

        Log log = new Log();
        log.setMensagem(mensagem);
        log.setDataHora(new Date());
        log.setEvento(evento);
        log.setFkTicket(ticket);
        log.setFkUsuarioSuporte(tecnico);

        logRepository.save(log);

        // Enviar log para Kafka
        kafkaTemplate.send("log-event", "Log registrado: " + mensagem);
    }

    public void excluir(Long id) {
        logRepository.deleteById(id);
    }
}