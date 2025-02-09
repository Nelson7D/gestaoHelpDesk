package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    List<Colaborador> findBySetor(String setor);

    // Novo método: Buscar colaboradores com mais de X tickets criados
    @Query("SELECT c FROM Colaborador c JOIN FETCH c.ticketsCriados WHERE SIZE(c.ticketsCriados) > :minTickets")
    List<Colaborador> findColaboradoresComMaisDeXTickets(int minTickets);

}
