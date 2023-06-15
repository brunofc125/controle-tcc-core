package com.controletcc.repository;

import com.controletcc.model.entity.ProjetoTccAspectoAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjetoTccAspectoAvaliacaoRepository extends JpaRepository<ProjetoTccAspectoAvaliacao, Long> {

    List<ProjetoTccAspectoAvaliacao> getAllByProjetoTccAvaliacaoId(Long idProjetoTccAvaliacao);

    List<ProjetoTccAspectoAvaliacao> getAllByIdIn(List<Long> idList);

    @Query(value = """
                SELECT count(distinct ptaa.id)
                FROM ProjetoTccAspectoAvaliacao ptaa
                JOIN ptaa.projetoTccAvaliacao pta
                JOIN pta.projetoTcc pt
                JOIN pt.situacaoAtual sa
                WHERE pt.id = :idProjetoTcc
                    AND sa.tipoTcc = pta.tipoTcc
            """
    )
    Long countByProjetoTcc(Long idProjetoTcc);

    @Query(value = """
                SELECT count(distinct ptaa.id)
                FROM ProjetoTccAspectoAvaliacao ptaa
                JOIN ptaa.projetoTccAvaliacao pta
                JOIN pta.projetoTcc pt
                JOIN pt.situacaoAtual sa
                WHERE pt.id = :idProjetoTcc
                    AND sa.tipoTcc = pta.tipoTcc
                    AND ptaa.valor is null
            """
    )
    Long countValorNuloByProjetoTcc(Long idProjetoTcc);

    @Query(value = """
                SELECT distinct ptaa
                FROM ProjetoTccAspectoAvaliacao ptaa
                JOIN ptaa.projetoTccAvaliacao pta
                JOIN pta.projetoTcc pt
                JOIN pt.situacaoAtual sa
                WHERE pt.id = :idProjetoTcc
                    AND sa.tipoTcc = pta.tipoTcc
            """
    )
    List<ProjetoTccAspectoAvaliacao> getAllByProjetoTcc(Long idProjetoTcc);

    @Query(value = """
                SELECT distinct pta.id
                FROM ProjetoTccAvaliacao pta
                JOIN pta.projetoTcc pt
                JOIN pt.situacaoAtual sa
                WHERE sa.situacaoTcc = 'EM_AVALIACAO'
                    AND sa.tipoTcc = pta.tipoTcc
                    AND pta.modeloItemAvaliacao.id = :idModeloItemAvaliacao
            """
    )
    List<Long> getIdsAvaliacaoByModeloItemAvaliacao(Long idModeloItemAvaliacao);

    List<ProjetoTccAspectoAvaliacao> getAllByProjetoTccAvaliacaoIdIn(List<Long> idProjetoTccAvaliacaoList);

}
