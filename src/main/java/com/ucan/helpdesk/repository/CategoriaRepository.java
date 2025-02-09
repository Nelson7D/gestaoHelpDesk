package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByNivel(Integer nivel);

    List<Categoria> findByFkCategoriaPai(Categoria categoriaPai);

    List<Categoria> findByPrioridadePadrao(Prioridade prioridadePadrao);

    //Obter todas as subcategorias de uma categoria
    @Query("SELECT c FROM Categoria c WHERE c.fkCategoriaPai = :categoriaPai")
    List<Categoria> findSubCategoriasByCategoriaPai(Categoria categoriaPai);

    Optional<Categoria> findByNome(String nome);
}
