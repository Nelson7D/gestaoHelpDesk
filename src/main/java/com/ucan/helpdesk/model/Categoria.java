package com.ucan.helpdesk.model;
import com.ucan.helpdesk.enums.Prioridade;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkCategoria;

    private String nome;
    private Integer nivel;

    @ManyToOne
    @JoinColumn(name = "fk_categoria_pai")
    private Categoria fkCategoriaPai;

    private Prioridade prioridadePadrao;

    @OneToMany(mappedBy = "fkCategoria")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "fkCategoriaPai")
    private List<Categoria> subcategorias;

    @OneToOne(mappedBy = "fkCategoria")
    private SLA sla;

    public Categoria() {}
}
