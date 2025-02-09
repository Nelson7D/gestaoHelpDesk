package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.model.Categoria;
import com.ucan.helpdesk.model.SLA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SLARepository extends JpaRepository<SLA, Long> {

    List<SLA> findByTempoMaximoRespostaLessThanEqual(Integer tempo);

    List<SLA> findByTempoMaximoResolucaoLessThanEqual(Integer tempo);

    Optional<SLA> findByFkCategoria(Categoria categoria);
}
