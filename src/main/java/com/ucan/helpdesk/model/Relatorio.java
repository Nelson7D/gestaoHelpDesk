package com.ucan.helpdesk.model;

import lombok.Data;

@Data
public class Relatorio {
    private String periodo;
    private double taxaConformidadeSLA;
    private double tempoMedioResolucao;

    public Relatorio(String periodo, double taxaConformidadeSLA, double tempoMedioResolucao) {
        this.periodo = periodo;
        this.taxaConformidadeSLA = taxaConformidadeSLA;
        this.tempoMedioResolucao = tempoMedioResolucao;
    }
}