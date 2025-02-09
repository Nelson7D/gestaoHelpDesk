package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.model.Log;
import com.ucan.helpdesk.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    // Listar todos os logs
    @GetMapping
    public ResponseEntity<List<Log>> listarTodosLogs() {
        List<Log> logs = logRepository.findAll();
        return ResponseEntity.ok(logs);
    }

    // Filtrar logs por ticket
    @GetMapping("/ticket/{id}")
    public ResponseEntity<Log> listarLogsporTicket(@PathVariable Long id) {
        return logRepository.findByFkTicketId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}