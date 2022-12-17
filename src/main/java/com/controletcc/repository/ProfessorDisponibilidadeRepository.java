package com.controletcc.repository;

import com.controletcc.model.entity.ProfessorDisponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ProfessorDisponibilidadeRepository extends JpaRepository<ProfessorDisponibilidade, Long> {

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

}
