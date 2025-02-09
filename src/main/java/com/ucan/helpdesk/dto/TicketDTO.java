package com.ucan.helpdesk.dto;

import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.enums.StatusTicket;
import lombok.Data;

import java.util.Date;

@Data
public class TicketDTO {
    private Long id;
    private String descricao;
    private Date dataHoraCriacao;
    private String categoriaId; // ID da categoria
    private Prioridade prioridade;
    private StatusTicket status;
    private String solicitanteId; // ID do colaborador
    private String tecnicoResponsavelId; // ID do técnico
    private String feedback;
}