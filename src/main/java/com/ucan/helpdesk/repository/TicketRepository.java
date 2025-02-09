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
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findById(Long ig);

    List<Ticket> findByStatus(StatusTicket status);

    List<Ticket> findByPrioridade(Prioridade prioridade);
    List<Ticket> findByFkTecnicoResponsavel(UsuarioSuporte tecnico);

    //Tickets com SLA violado
    @Query(value = "SELECT * FROM ticket t WHERE t.data_hora_criacao + (t.tempo_maximo_resposta * INTERVAL '1 MINUTE') < CURRENT_TIMESTAMP", nativeQuery = true)
    List<Ticket> findTicketsComSLAViolado();


    //Tickets abertos por categoria
    List<Ticket> findByFkCategoriaAndStatus(Categoria categoria, StatusTicket status);
}
