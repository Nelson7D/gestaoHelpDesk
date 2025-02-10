package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.model.Log;
import com.ucan.helpdesk.repository.LogRepository;
import com.ucan.helpdesk.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogService logService;

    // Listar todos os logs
    @GetMapping
    public ResponseEntity<List<Log>> listarTodosLogs() {
        List<Log> logs = logRepository.findAll();
        return ResponseEntity.ok(logs);
    }

    // Filtrar logs por ticket
    @GetMapping("/ticket/{id}")
    public ResponseEntity<Log> listarLogsporTicket(@PathVariable Long id) {
        return logRepository.findByFkTicketPkTicket(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/evento/{evento}")
    public ResponseEntity<List<Log>> getLogsByEvento(@PathVariable String evento) {
        List<Log> logs = logService.buscarPorEvento(evento);
        return ResponseEntity.ok(logs);
    }

}