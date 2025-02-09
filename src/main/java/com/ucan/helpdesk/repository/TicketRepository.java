package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.enums.StatusTicket;
import com.ucan.helpdesk.model.Categoria;
import com.ucan.helpdesk.model.Ticket;
import com.ucan.helpdesk.model.UsuarioSuporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatus(String status);

    List<Ticket> findByPrioridade(Prioridade prioridade);
    List<Ticket> findByFkTecnicoResponsavel(UsuarioSuporte tecnico);

    //Tickets com SLA violado
    @Query("SELECT t FROM Ticket t WHERE t.dataHoraCriacao + INTERVAL 't.tempoMaximoResposta MINUTE' < CURRENT_TIMESTAMP")
    List<Ticket> findTicketsComSLAViolado();

    //Tickets abertos por categoria
    List<Ticket> findByFkCategoriaAndStatus(Categoria categoria, StatusTicket status);
}
