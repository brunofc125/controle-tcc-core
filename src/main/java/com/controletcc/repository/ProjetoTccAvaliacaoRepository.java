package com.controletcc.repository;

import com.controletcc.model.entity.ProjetoTccAvaliacao;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjetoTccAvaliacaoRepository extends JpaRepository<ProjetoTccAvaliacao, Long> {

    List<ProjetoTccAvaliacao> getAllByProjetoTccIdAndTipoTcc(Long idProjetoTcc, TipoTcc tipoTcc);

    @Query(value = """  
            SELECT count(distinct pta.id) > 0
            FROM ProjetoTcc pt
            JOIN pt.situacaoAtual s
            LEFT JOIN ProjetoTccAvaliacao pta on pta.projetoTcc.id = pt.id and pta.tipoTcc = s.tipoTcc
                WHERE pt.id = :idProjetoTcc
            """
    )
    boolean existsAvaliacaoIniciada(Long idProjetoTcc);

    ProjetoTccAvaliacao getProjetoTccAvaliacaoByTipoTccAndTipoProfessorAndProjetoTccIdAndProfessorId(TipoTcc tipoTcc, TipoProfessor tipoProfessor, Long idProjetoTcc, Long idProfessor);

}
