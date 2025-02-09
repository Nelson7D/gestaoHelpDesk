package com.ucan.helpdesk.repository;

import com.ucan.helpdesk.model.RegraPrioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegraPrioridadeRepository extends JpaRepository<RegraPrioridade, Long> {
    Optional<RegraPrioridade> findByPalavraChave(String palavraChave);

    List<RegraPrioridade> findByPalavraChaveContainingIgnoreCase(String palavraChave);
}