package com.ucan.helpdesk.utils;

import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.model.Categoria;
import com.ucan.helpdesk.repository.CategoriaRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaImportService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public void importarCategorias(InputStream file) throws IOException {
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
}