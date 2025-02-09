package com.ucan.helpdesk.service;

import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.model.Categoria;
import com.ucan.helpdesk.repository.CategoriaRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public List<Categoria> buscarPorNivel(Integer nivel) {
        return categoriaRepository.findByNivel(nivel);
    }

    public List<Categoria> buscarPorCategoriaPai(Categoria categoriaPai) {
        return categoriaRepository.findByFkCategoriaPai(categoriaPai);
    }

    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarSubcategorias(Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return categoria.getSubcategorias();
    }
}
