package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.model.Colaborador;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.ColaboradorRepository;
import com.ucan.helpdesk.repository.TicketRepository;
import com.ucan.helpdesk.service.ColaboradorService;
import com.ucan.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;


    @Autowired
    private ColaboradorService colaboradorService;

    @GetMapping
    public ResponseEntity<List<Colaborador>> listarTodos() {
        return ResponseEntity.ok(colaboradorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> buscarPorId(@PathVariable Long id) {
        Optional<Colaborador> colaborador = colaboradorService.buscarPorId(id);
        return colaborador.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Criar um novo colaborador
    @PostMapping
    public ResponseEntity<Colaborador> criarColaborador(@RequestBody Colaborador colaborador) {
        Colaborador colaboradorSalvo = colaboradorRepository.save(colaborador);
        return ResponseEntity.status(201).body(colaboradorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Colaborador> atualizarColaborador(@PathVariable Long id, @RequestBody Colaborador colaborador) {
        return ResponseEntity.ok(colaboradorService.atualizarColaborador(id, colaborador));
    }

    // Registrar um novo ticket por um colaborador
    @PostMapping("/{colaboradorId}/tickets")
    public ResponseEntity<Ticket> registrarTicket(@PathVariable Long colaboradorId, @RequestBody Ticket ticket) {
        Ticket novoTicket = ticketService.criarTicketPorColaborador(colaboradorId,ticket.getDescricao());
        return ResponseEntity.status(201).body(novoTicket);
    }
}