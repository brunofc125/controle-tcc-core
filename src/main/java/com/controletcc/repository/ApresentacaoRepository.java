package com.controletcc.repository;

import com.controletcc.model.entity.Apresentacao;
import com.controletcc.model.enums.TipoTcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ApresentacaoRepository extends JpaRepository<Apresentacao, Long> {

    boolean existsByProjetoTccIdAndTipoTcc(Long idProjetoTcc, TipoTcc tipoTcc);

    @Query(value = """
            SELECT count(a.id) > 0
            FROM Apresentacao a
            WHERE a.agendaApresentacao.id = :idAgendaApresentacao
            AND (:id is null OR a.id <> :id)
            AND (
                (:dataInicial >= a.dataInicial AND :dataInicial < a.dataFinal) OR (a.dataInicial >= :dataInicial AND a.dataInicial < :dataFinal)
                OR (:dataFinal > a.dataInicial AND :dataFinal <= a.dataFinal) OR (a.dataFinal > :dataInicial AND a.dataFinal <= :dataFinal)
            )"""
    )
    boolean existsIntersect(Long id, Long idAgendaApresentacao, LocalDateTime dataInicial, LocalDateTime dataFinal);

    Apresentacao getFirstByProjetoTccIdAndTipoTcc(Long idProjetoTcc, TipoTcc tipoTcc);

    List<Apresentacao> getAllByAgendaApresentacaoIdAndProjetoTccIdNot(Long idAgendaApresentacao, Long idProjetoTcc);

}
