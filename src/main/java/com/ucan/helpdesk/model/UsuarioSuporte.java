package com.ucan.helpdesk.model;

import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario_suporte")
@Data
public class UsuarioSuporte extends Usuario {

    private TipoUsuarioSuporte tipo; // Colaborador, Técnico, Supervisor, Administrador

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> especialidades = new HashSet<>();

    @OneToMany(mappedBy = "fkTecnicoResponsavel")
    private List<Ticket> ticketsAtendidos;

    public UsuarioSuporte() {}

}