package com.ucan.helpdesk.model;

import com.ucan.helpdesk.enums.EventoLog;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkLog;

    private String mensagem;
    private Date dataHora;
    private EventoLog evento;

    @ManyToOne
    @JoinColumn(name = "fk_ticket")
    private Ticket fkTicket;

    @ManyToOne
    @JoinColumn(name = "fk_usuario_suporte")
    private UsuarioSuporte fkUsuarioSuporte;

}
