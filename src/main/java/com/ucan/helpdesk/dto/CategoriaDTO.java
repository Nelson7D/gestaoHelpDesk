package com.ucan.helpdesk.dto;

import com.ucan.helpdesk.enums.Prioridade;
import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private String nome;
    private Integer nivel;
    private String categoriaPaiId; // ID da categoria pai
    private Prioridade prioridadePadrao;
}