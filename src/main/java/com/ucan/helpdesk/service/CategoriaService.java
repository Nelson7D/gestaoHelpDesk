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

    // Importar categorias do arquivo Excel
    public void importarCategorias(String filePath) throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheet("Categorias");

        List<Categoria> categorias = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Ignorar cabeçalho

            Categoria categoria = new Categoria();
            categoria.setNome(row.getCell(1).getStringCellValue());
            categoria.setNivel((int) row.getCell(2).getNumericCellValue());

            String categoriaPaiId = row.getCell(3).getStringCellValue();
            if (!categoriaPaiId.isEmpty()) {
                Categoria categoriaPai = categoriaRepository.findById(Long.parseLong(categoriaPaiId))
                        .orElseThrow(() -> new RuntimeException("Categoria pai não encontrada"));
                categoria.setFkCategoriaPai(categoriaPai);
            }

            categoria.setPrioridadePadrao(Prioridade.valueOf(row.getCell(4).getStringCellValue().toUpperCase()));
            categorias.add(categoria);
        }

        categoriaRepository.saveAll(categorias);

        workbook.close();
        file.close();
    }

    public List<Categoria> listarSubcategorias(Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return categoria.getSubcategorias();
    }
}
