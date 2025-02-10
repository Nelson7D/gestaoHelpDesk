package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.enums.StatusUsuario;
import com.ucan.helpdesk.model.Usuario;
import com.ucan.helpdesk.model.UsuarioSuporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioSuporteRepository extends JpaRepository<UsuarioSuporte, Long> {

    UsuarioSuporte findByEmail(String email);
    List<UsuarioSuporte> findByEmailAndStatusUsuario(String email, StatusUsuario statusUsuario);
    List<UsuarioSuporte> findByStatusUsuario(StatusUsuario statusUsuario);

    List<UsuarioSuporte> findByTipo(TipoUsuarioSuporte tipo);
    List<UsuarioSuporte> findByEspecialidadesContaining(String especialidade);

    @Query("SELECT u FROM UsuarioSuporte u WHERE NOT EXISTS (SELECT t FROM Ticket t WHERE t.fkTecnicoResponsavel = u AND t.status = com.ucan.helpdesk.enums.StatusTicket.EM_ANDAMENTO)")
    List<UsuarioSuporte> findTecnicosDisponiveis();

}
