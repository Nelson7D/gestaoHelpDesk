package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import com.ucan.helpdesk.model.UsuarioSuporte;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.repository.UsuarioSuporteRepository;
import com.ucan.helpdesk.repository.TicketRepository;
import com.ucan.helpdesk.service.TicketService;
import com.ucan.helpdesk.service.UsuarioSuporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tecnicos")
public class UsuarioSuporteController {

    @Autowired
    private UsuarioSuporteRepository usuarioSuporteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private UsuarioSuporteService usuarioSuporteService;

    @GetMapping
    public ResponseEntity<List<UsuarioSuporte>> listarTodos() {
        return ResponseEntity.ok(usuarioSuporteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSuporte> buscarPorId(@PathVariable Long id) {
        Optional<UsuarioSuporte> usuario = usuarioSuporteService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioSuporte> criarUsuarioSuporte(@RequestBody UsuarioSuporte usuarioSuporte) {
        return ResponseEntity.ok(usuarioSuporteService.criarUsuarioSuporte(usuarioSuporte));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSuporte> atualizarUsuarioSuporte(@PathVariable Long id, @RequestBody UsuarioSuporte usuarioSuporte) {
        return ResponseEntity.ok(usuarioSuporteService.atualizarUsuarioSuporte(id, usuarioSuporte));
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