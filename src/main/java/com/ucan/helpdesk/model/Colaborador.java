package com.ucan.helpdesk.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@DiscriminatorValue("COLABORADOR")
public class Colaborador extends Usuario {
    private String setor;

    @OneToMany(mappedBy = "fkColaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketsCriados;

    public Colaborador() {}
}