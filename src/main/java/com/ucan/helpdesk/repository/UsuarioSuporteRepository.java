package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.model.UsuarioSuporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import java.util.List;

@Repository
public interface UsuarioSuporteRepository extends JpaRepository<UsuarioSuporte, Long> {


    List<UsuarioSuporte> findByTipo(TipoUsuarioSuporte tipo);
    List<UsuarioSuporte> findByEspecialidadesContaining(String especialidade);

    @Query("SELECT u FROM UsuarioSuporte u WHERE u NOT MEMBER OF (SELECT t.fkTecnicoResponsavel FROM Ticket t WHERE t.status = 'EM_ANDAMENTO')")
    List<UsuarioSuporte> findTecnicosDisponiveis();
}
