package com.controletcc.repository;

import com.controletcc.model.entity.MembroBanca;
import com.controletcc.repository.projection.MembroBancaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MembroBancaRepository extends JpaRepository<MembroBanca, Long> {

    @Query(value = """  
            SELECT
                mb.id as id,
                p.nome as nomeProfessor,
                mb.dataSolicitacao as dataSolicitacao,
                mb.dataConfirmacao as dataConfirmacao,
                (mb.dataConfirmacao is not null) as confirmado
            FROM MembroBanca mb
            JOIN mb.professor p
                where mb.projetoTcc.id = :idProjetoTcc
            ORDER BY p.nome"""
    )
    List<MembroBancaProjection> getAllByIdProjetoTcc(Long idProjetoTcc);

    boolean existsByProjetoTccIdAndProfessorId(Long idProjetoTcc, Long idProfessor);

}
