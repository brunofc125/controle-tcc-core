package com.controletcc.repository;

import com.controletcc.model.entity.Apresentacao;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.ApresentacaoProjection;
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

    @Query(value = """
            SELECT DISTINCT
                a.id as id,
                pt.alunosNome as descricao,
                pt.id as idProjetoTcc,
                a.tipoTcc as tipoTcc,
                a.dataInicial as dataInicial,
                a.dataFinal as dataFinal,
                (case when po.id = :idProfessor then 'ORIENTADOR'
                when pmb.id = :idProfessor then 'MEMBRO_BANCA'
                else 'SUPERVISOR' end) as participacao
            FROM Apresentacao a
            JOIN a.agendaApresentacao aa
            JOIN a.projetoTcc pt
            JOIN pt.situacaoAtual sa
            JOIN pt.professorSupervisor ps
            JOIN pt.professorOrientador po
            LEFT JOIN MembroBanca mb on mb.projetoTcc.id = pt.id and mb.tipoTcc = sa.tipoTcc
            LEFT JOIN mb.professor pmb
            WHERE aa.ano = :ano
            AND aa.periodo = :periodo
            AND (
                ps.id = :idProfessor
                OR po.id = :idProfessor
                or pmb.id = :idProfessor
            )"""
    )
    List<ApresentacaoProjection> getAllByProfessorAndAnoPeriodo(Long idProfessor, Integer ano, Integer periodo);

}
