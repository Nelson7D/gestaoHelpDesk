package com.ucan.helpdesk.model;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario")
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
}