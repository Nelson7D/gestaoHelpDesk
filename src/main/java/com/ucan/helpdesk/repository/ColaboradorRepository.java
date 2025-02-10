package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.enums.StatusUsuario;
import com.ucan.helpdesk.model.Colaborador;
import com.ucan.helpdesk.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    List<Colaborador> findByEmailAndStatusUsuario(String email, StatusUsuario statusUsuario);
    List<Colaborador> findByStatusUsuario(StatusUsuario statusUsuario);
    List<Colaborador> findBySetor(String setor);

    Colaborador findByEmail(String email);

    // Buscar colaboradores com mais de X tickets criados
    @Query("SELECT c FROM Colaborador c JOIN FETCH c.ticketsCriados WHERE SIZE(c.ticketsCriados) > :minTickets")
    List<Colaborador> findColaboradoresComMaisDeXTickets(int minTickets);

}
