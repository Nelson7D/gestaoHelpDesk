package com.ucan.helpdesk.model;

import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class UsuarioSuporte extends Usuario {

    private TipoUsuarioSuporte tipo; // Colaborador, Técnico, Supervisor, Administrador

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> especialidades = new HashSet<>();

    @OneToMany(mappedBy = "fkTecnicoResponsavel")
    private List<Ticket> ticketsAtendidos;

    public UsuarioSuporte() {}

}