package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import com.ucan.helpdesk.model.UsuarioSuporte;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.UsuarioSuporteRepository;
import com.ucan.helpdesk.repository.TicketRepository;
import com.ucan.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class UsuarioSuporteController {

    @Autowired
    private UsuarioSuporteRepository usuarioSuporteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    // Listar todos os técnicos
    @GetMapping
    public ResponseEntity<List<UsuarioSuporte>> listarTodosTecnicos() {
        List<UsuarioSuporte> tecnicos = usuarioSuporteRepository.findAll();
        return ResponseEntity.ok(tecnicos);
    }

    // Buscar técnico por tipo (ex.: Técnico, Supervisor)
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<UsuarioSuporte>> listarTecnicosPorTipo(@PathVariable TipoUsuarioSuporte tipo) {
        List<UsuarioSuporte> tecnicos = usuarioSuporteRepository.findByTipo(tipo);
        return ResponseEntity.ok(tecnicos);
    }

    // Atribuir um ticket a um técnico
    @PostMapping("/{tecnicoId}/tickets/{ticketId}")
    public ResponseEntity<Ticket> atribuirTicketParaTecnico(@PathVariable Long tecnicoId, @PathVariable Long ticketId) {
        ticketService.triagemAutomatica(ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket não encontrado")));
        return ResponseEntity.ok(ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket não encontrado")));
    }
}