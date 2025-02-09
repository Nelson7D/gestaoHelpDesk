package com.ucan.helpdesk.controller;

import com.ucan.helpdesk.model.Categoria;
import com.ucan.helpdesk.repository.CategoriaRepository;
import com.ucan.helpdesk.service.CategoriaService;
import com.ucan.helpdesk.utils.CategoriaImportService;
import com.ucan.helpdesk.utils.RegraPrioridadeImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaImportService categoriaImportService;

    @Autowired
    private RegraPrioridadeImportService regraPrioridadeImportService;


    // Listar todas as categorias
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodasCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return ResponseEntity.ok(categorias);
    }

    // Importar categorias do arquivo Excel
    @PostMapping("/import/categorias")
    public ResponseEntity<String> importarCategorias(@RequestParam("file") MultipartFile file) {
        try {
            categoriaImportService.importarCategorias(file.getInputStream());
            return ResponseEntity.ok("Categorias importadas com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao importar categorias: " + e.getMessage());
        }
    }

    @PostMapping("/import/regras-prioridade")
    public ResponseEntity<String> importarRegrasPrioridade(@RequestParam("file") MultipartFile file) {
        try {
            regraPrioridadeImportService.importRegrasPrioridade(file.getInputStream());
            return ResponseEntity.ok("Regras de prioridade importadas com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao importar regras de prioridade: " + e.getMessage());
        }
    }
}