package com.ucan.helpdesk.model;

import com.ucan.helpdesk.enums.Prioridade;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RegraPrioridade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkRegraPrioridade;

    private String palavraChave;
    private String categoriaId;
    private Prioridade prioridade;
}