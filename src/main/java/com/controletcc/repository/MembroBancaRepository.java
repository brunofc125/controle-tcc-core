package com.controletcc.repository;

import com.controletcc.model.entity.MembroBanca;
import com.controletcc.model.enums.TipoTcc;
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
                JOIN mb.projetoTcc pt
                JOIN pt.situacaoAtual sa
                WHERE pt.id = :idProjetoTcc
                    AND mb.tipoTcc = sa.tipoTcc
                ORDER BY p.nome
            """
    )
    List<MembroBancaProjection> getAllByIdProjetoTcc(Long idProjetoTcc);

    @Query(value = """  
                SELECT
                    mb
                FROM MembroBanca mb
                JOIN mb.projetoTcc pt
                JOIN pt.situacaoAtual sa
                WHERE pt.id = :idProjetoTcc
                    AND mb.tipoTcc = sa.tipoTcc
            """
    )
    List<MembroBanca> getAllByProjetoTccId(Long idProjetoTcc);

    @Query(value = """  
                SELECT
                    count(mb.id) > 0
                FROM MembroBanca mb
                JOIN mb.projetoTcc pt
                JOIN pt.situacaoAtual sa
                WHERE pt.id = :idProjetoTcc
                    AND mb.tipoTcc = sa.tipoTcc
                    AND mb.professor.id = :idProfessor
            """
    )
    boolean existsByProjetoTccIdAndProfessorId(Long idProjetoTcc, Long idProfessor);

    MembroBanca getByProjetoTccIdAndTipoTccAndProfessorId(Long idProjetoTcc, TipoTcc tipoTcc, Long idProfessor);

}
