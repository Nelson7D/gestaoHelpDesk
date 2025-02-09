package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.enums.StatusTicket;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.TicketRepository;
import com.ucan.helpdesk.service.TicketService;
import com.ucan.helpdesk.enums.StatusTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    // Listar todos os tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> listarTodosTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    // Buscar ticket por ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> listarTicketPorId(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar um novo ticket
    @PostMapping
    public ResponseEntity<Ticket> criarTicket(@RequestBody Ticket ticket) {
        Ticket ticketCriado = ticketService.novoTicket(ticket.getDescricao());
        return ResponseEntity.status(201).body(ticketCriado);
    }

    // Atualizar status de um ticket
    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> atualizarTicketStatus(@PathVariable Long id, @RequestParam String novoStatus) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setStatus(StatusTicket.valueOf(novoStatus));
                    Ticket updatedTicket = ticketRepository.save(ticket);
                    return ResponseEntity.ok(updatedTicket);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Escalonar ticket para supervisor
    @PostMapping("/{id}/escalate")
    public ResponseEntity<Ticket> escalonarTicket(@PathVariable Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
        ticketService.escalonarTicket(ticket);
        return ResponseEntity.ok(ticket);
    }

    // Fechar ticket
    @PostMapping("/{id}/close")
    public ResponseEntity<Ticket> fecharTicket(@PathVariable Long id, @RequestParam String feedback) {
        Ticket ticketFechado = ticketService.fecharTicket(id, feedback);
        return ResponseEntity.ok(ticketFechado);
    }


}