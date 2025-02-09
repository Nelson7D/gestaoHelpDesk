package com.ucan.helpdesk.model;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkUsuario;

    @NotNull
    private String nome;

    @NotNull
    private String email;

    @NotNull
    private String senha;

    private String status;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>(); // Ex.: "COLABORADOR", "TECNICO", "SUPERVISOR", "ADMINISTRADOR"
}