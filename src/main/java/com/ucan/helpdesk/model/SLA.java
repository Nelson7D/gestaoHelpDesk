package com.ucan.helpdesk.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class SLA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkSLA;

    @OneToOne
    @JoinColumn(name = "fk_categoria")
    private Categoria fkCategoria;

    private Integer tempoMaximoResposta;
    private Integer tempoMaximoResolucao;


}