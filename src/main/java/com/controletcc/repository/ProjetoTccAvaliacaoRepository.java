package com.controletcc.repository;

import com.controletcc.model.entity.ProjetoTccAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjetoTccAvaliacaoRepository extends JpaRepository<ProjetoTccAvaliacao, Long> {

    @Query(value = """  
            SELECT count(distinct pta.id) > 0
            FROM ProjetoTcc pt
            JOIN pt.situacaoAtual s
            LEFT JOIN ProjetoTccAvaliacao pta on pta.projetoTcc.id = pt.id and pta.tipoTcc = s.tipoTcc
                WHERE pt.id = :idProjetoTcc
            """
    )
    boolean existsAvaliacaoIniciada(Long idProjetoTcc);

}
