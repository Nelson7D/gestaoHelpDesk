package com.ucan.helpdesk.model;
import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.enums.StatusTicket;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkTicket;

    private String descricao;
    private Date dataHoraCriacao;

    @ManyToOne
    @JoinColumn(name = "fk_categoria")
    private Categoria fkCategoria;

    private Prioridade prioridade;

    private StatusTicket status;

    @ManyToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador fkColaborador;

    @ManyToOne
    @JoinColumn(name = "fk_tecnico_responsavel")
    private UsuarioSuporte fkTecnicoResponsavel;

    @ManyToOne
    @JoinColumn(name = "fk_sla")
    private SLA fkSLA;

    @OneToMany(mappedBy = "fkTicket", cascade = CascadeType.ALL)
    private List<Log> logs;
    private String feedback;

    public Ticket() {}

}