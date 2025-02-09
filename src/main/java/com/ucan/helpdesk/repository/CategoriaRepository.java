package com.ucan.helpdesk.repository;
import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByNivel(Integer nivel);

    List<Categoria> findByFkCategoriaPai(Categoria categoriaPai);

    List<Categoria> findByPrioridade(Prioridade prioridade);

    // Buscar categorias por palavra-chave
    List<Categoria> findByNomeContainingIgnoreCase(String keyword);

    //Obter todas as subcategorias de uma categoria
    @Query("SELECT c FROM Categoria c WHERE c.fkCategoriaPai = :categoriaPai")
    List<Categoria> findSubCategoriasByCategoriaPai(Categoria categoriaPai);
}
