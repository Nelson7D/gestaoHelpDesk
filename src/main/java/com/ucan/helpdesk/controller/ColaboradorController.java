package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.model.Colaborador;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.ColaboradorRepository;
import com.ucan.helpdesk.repository.TicketRepository;
import com.ucan.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    // Listar todos os colaboradores
    @GetMapping
    public ResponseEntity<List<Colaborador>> getAllColaboradores() {
        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        return ResponseEntity.ok(colaboradores);
    }

    // Buscar colaborador por ID
    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> getColaboradorById(@PathVariable Long id) {
        return colaboradorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar um novo colaborador
    @PostMapping
    public ResponseEntity<Colaborador> criarColaborador(@RequestBody Colaborador colaborador) {
        Colaborador colaboradorSalvo = colaboradorRepository.save(colaborador);
        return ResponseEntity.status(201).body(colaboradorSalvo);
    }

    // Registrar um novo ticket por um colaborador
    @PostMapping("/{colaboradorId}/tickets")
    public ResponseEntity<Ticket> registrarTicket(@PathVariable Long colaboradorId, @RequestBody Ticket ticket) {
        Ticket novoTicket = ticketService.criarTicket(ticket.getDescricao(), ticket.getFkCategoria().getPkCategoria().toString());
        return ResponseEntity.status(201).body(novoTicket);
    }
}