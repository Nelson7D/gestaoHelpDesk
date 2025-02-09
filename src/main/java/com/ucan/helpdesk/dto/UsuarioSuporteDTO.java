package com.ucan.helpdesk.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UsuarioSuporteDTO extends UsuarioDTO {
    private String tipo; // Técnico, Supervisor, Administrador
    private Set<String> especialidades;
}