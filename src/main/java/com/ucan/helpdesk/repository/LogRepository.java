package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.enums.EventoLog;
import com.ucan.helpdesk.model.Log;
import com.ucan.helpdesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByEvento(EventoLog evento);


    List<Log> findByFkTicket(Ticket ticket);

    Optional<Log> findByFkTicketPkTicket(Long pkTicket);


    // Filtrar logs por data/hora
    List<Log> findByDataHoraBetween(Date dataInicio, Date dataFim);

}
