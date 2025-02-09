package com.ucan.helpdesk.dto;

import lombok.Data;

@Data
public class SLADTO {
    private Long id;
    private String categoriaId; // ID da categoria associada
    private Integer tempoMaximoResposta;
    private Integer tempoMaximoResolucao;
}