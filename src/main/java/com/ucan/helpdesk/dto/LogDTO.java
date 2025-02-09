package com.ucan.helpdesk.dto;

import com.ucan.helpdesk.enums.EventoLog;
import lombok.Data;

import java.util.Date;

@Data
public class LogDTO {
    private Long id;
    private String mensagem;
    private Date dataHora;
    private EventoLog evento;
    private Long ticketId; // ID do ticket associado
    private Long usuarioSuporteId; // ID do técnico associado
}