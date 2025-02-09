package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.model.SLA;
import com.ucan.helpdesk.repository.SLARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slas")
public class SLAController {

    @Autowired
    private SLARepository slaRepository;

    // Listar todos os SLAs
    @GetMapping
    public ResponseEntity<List<SLA>> listarTodasSLAs() {
        List<SLA> slas = slaRepository.findAll();
        return ResponseEntity.ok(slas);
    }

    // Obter SLA por categoria
    @GetMapping("/categoria/{id}")
    public ResponseEntity<SLA> listarSLAPorCategoria(@PathVariable Long id) {
        return slaRepository.findByFkCategoriaId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}