package com.controletcc.repository;

import com.controletcc.model.entity.ProfessorDisponibilidade;
import com.controletcc.repository.projection.ProfessorDisponibilidadeAgrupadaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProfessorDisponibilidadeRepository extends JpaRepository<ProfessorDisponibilidade, Long> {

    List<ProfessorDisponibilidade> getAllByAnoAndPeriodoAndProfessorId(Integer ano, Integer periodo, Long idProfessor);

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
            select
                date_event as dataHora,
                nomes_professores as descricao,
                qtd_professor as qtdProfessores
            from get_disponibilidades(to_bigint_array(:idProfessores), :idProjetoTcc, :dataInicio, :dataFim, :idAgendaApresentacao)
            """,
            nativeQuery = true
    )
    List<ProfessorDisponibilidadeAgrupadaProjection> getDisponibilidades(List<Long> idProfessores, Long idProjetoTcc, Long idAgendaApresentacao, LocalDateTime dataInicio, LocalDateTime dataFim);

    boolean existsByAnoAndPeriodoAndProfessorId(Integer ano, Integer periodo, Long idProfessor);

}
