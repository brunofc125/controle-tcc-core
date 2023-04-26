package com.controletcc.repository;

import com.controletcc.model.entity.ProfessorDisponibilidade;
import com.controletcc.repository.projection.ProfessorDisponibilidadeAgrupadaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProfessorDisponibilidadeRepository extends JpaRepository<ProfessorDisponibilidade, Long> {

    List<ProfessorDisponibilidade> getAllByAnoAndPeriodoAndProfessorId(Integer ano, Integer periodo, Long idProfessor);

    List<ProfessorDisponibilidade> getAllByAnoAndPeriodoAndProfessorIdIn(Integer ano, Integer periodo, List<Long> idProfessorList);

    @Query(value = """
            SELECT count(pd.id) > 0
            FROM ProfessorDisponibilidade pd
            WHERE pd.professor.id = :idProfessor
            AND (:id is null OR pd.id <> :id)
            AND pd.ano = :ano
            AND pd.periodo = :periodo
            AND (
                (:dataInicial >= pd.dataInicial AND :dataInicial < pd.dataFinal) OR (pd.dataInicial >= :dataInicial AND pd.dataInicial < :dataFinal)
                OR (:dataFinal > pd.dataInicial AND :dataFinal <= pd.dataFinal) OR (pd.dataFinal > :dataInicial AND pd.dataFinal <= :dataFinal)
            )"""
    )
    boolean existsIntersect(Long id, Long idProfessor, LocalDateTime dataInicial, LocalDateTime dataFinal, Integer ano, Integer periodo);

    @Query(value = """
            SELECT count(vw_pc.identifier) > 0
            FROM VwProfessorCompromisso vw_pc
            WHERE (
                vw_pc.idProfessor = :idProfessor
                OR vw_pc.idProfessorBanca = :idProfessor
                OR vw_pc.idProfessorOrientador = :idProfessor
                OR vw_pc.idProfessorSupervisor = :idProfessor
            )
            AND (:id is null OR vw_pc.tipoCompromisso = 'APRESENTACAO' OR vw_pc.id <> :id)
            AND (
                (:dataInicial >= vw_pc.dataInicial AND :dataInicial < vw_pc.dataFinal) OR (vw_pc.dataInicial >= :dataInicial AND vw_pc.dataInicial < :dataFinal)
                OR (:dataFinal > vw_pc.dataInicial AND :dataFinal <= vw_pc.dataFinal) OR (vw_pc.dataFinal > :dataInicial AND vw_pc.dataFinal <= :dataFinal)
            )"""
    )
    boolean existsIntersect(Long id, Long idProfessor, LocalDateTime dataInicial, LocalDateTime dataFinal);

    @Query(value = """
            select
                date_event as dataHora,
                nomes_professores as descricao,
                qtd_professor as qtdProfessores
            from get_disponibilidades(to_bigint_array(:idProfessores), :idProjetoTcc, :dataInicio, :dataFim)
            """,
            nativeQuery = true
    )
    List<ProfessorDisponibilidadeAgrupadaProjection> getDisponibilidades(List<Long> idProfessores, Long idProjetoTcc, LocalDateTime dataInicio, LocalDateTime dataFim);

}
