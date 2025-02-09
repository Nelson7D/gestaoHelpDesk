package com.ucan.helpdesk.utils;

import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.model.RegraPrioridade;
import com.ucan.helpdesk.repository.RegraPrioridadeRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegraPrioridadeImportService {

    @Autowired
    private RegraPrioridadeRepository regraPrioridadeRepository;

    public void importRegrasPrioridade(InputStream file) throws Exception {
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheet("Regras de Prioridade");

        List<RegraPrioridade> regras = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Ignorar cabeçalho

            RegraPrioridade regra = new RegraPrioridade();
            regra.setPalavraChave(row.getCell(0).getStringCellValue());
            regra.setCategoriaId(row.getCell(1).getStringCellValue());
            regra.setPrioridade(Prioridade.valueOf(row.getCell(2).getStringCellValue().toUpperCase()));
            regras.add(regra);
        }

        regraPrioridadeRepository.saveAll(regras);
    }
}