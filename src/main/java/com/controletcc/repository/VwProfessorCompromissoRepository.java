package com.controletcc.repository;

import com.controletcc.model.enums.TipoCompromisso;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.model.view.VwProfessorCompromisso;
import com.controletcc.repository.projection.ProfessorDisponibilidadeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VwProfessorCompromissoRepository extends JpaRepository<VwProfessorCompromisso, Long> {

    @Query(value = """  
            SELECT distinct
                vw_pc.id as id,
                vw_pc.tipoCompromisso as tipoCompromisso,
                vw_pc.tipoTcc as tipoTcc,
                vw_pc.descricao as descricao,
                vw_pc.alunosNome as alunosNome,
                vw_pc.dataInicial as dataInicial,
                vw_pc.dataFinal as dataFinal,
                (case
                    when vw_pc.idProfessorSupervisor = :idProfessor
                        and vw_pc.idProfessorOrientador = :idProfessor then 'Supervisor/Orientador'
                    when vw_pc.idProfessorSupervisor = :idProfessor then 'Supervisor'
                    when vw_pc.idProfessorOrientador = :idProfessor then 'Orientador'
                    when vw_pc.idProfessorBanca = :idProfessor then 'Membro da Banca'
                    else ''
                end
                ) as papel
            FROM VwProfessorCompromisso vw_pc
                WHERE ( vw_pc.idProfessor = :idProfessor OR vw_pc.idProfessorSupervisor = :idProfessor
                OR vw_pc.idProfessorOrientador = :idProfessor OR vw_pc.idProfessorBanca = :idProfessor )
                and (:id is null or vw_pc.id = :id)
                and (:descricao is null or lower(vw_pc.descricao) like concat('%', trim(lower(:descricao)),'%') )
                and (:tipoCompromisso is null or vw_pc.tipoCompromisso = :tipoCompromisso)
                and (:tipoTcc is null or vw_pc.tipoTcc = :tipoTcc)
                and (:idAgenda is null or vw_pc.idAgendaApresentacao = :idAgenda)
                and (cast( :dataInicial as date ) is null or cast( vw_pc.dataInicial as date ) >= cast( :dataInicial as date ) )
                and (cast( :dataFinal as date ) is null or cast( vw_pc.dataFinal as date ) <= cast( :dataFinal as date ) )"""
    )
    Page<ProfessorDisponibilidadeProjection> search(Long idProfessor,
                                                    Long id,
                                                    String descricao,
                                                    TipoCompromisso tipoCompromisso,
                                                    TipoTcc tipoTcc,
                                                    Long idAgenda,
                                                    LocalDate dataInicial,
                                                    LocalDate dataFinal,
                                                    Pageable pageable);

    @Query(value = """
            SELECT distinct
                vw_pc.id as id,
                vw_pc.tipoCompromisso as tipoCompromisso,
                vw_pc.tipoTcc as tipoTcc,
                vw_pc.descricao as descricao,
                vw_pc.alunosNome as alunosNome,
                vw_pc.dataInicial as dataInicial,
                vw_pc.dataFinal as dataFinal,
                (case
                    when vw_pc.idProfessorSupervisor = :idProfessor
                        and vw_pc.idProfessorOrientador = :idProfessor then 'Supervisor/Orientador'
                    when vw_pc.idProfessorSupervisor = :idProfessor then 'Supervisor'
                    when vw_pc.idProfessorOrientador = :idProfessor then 'Orientador'
                    when vw_pc.idProfessorBanca = :idProfessor then 'Membro da Banca'
                    else ''
                end
                ) as papel
            FROM VwProfessorCompromisso vw_pc
            WHERE (
                vw_pc.idProfessor = :idProfessor
                OR vw_pc.idProfessorBanca = :idProfessor
                OR vw_pc.idProfessorOrientador = :idProfessor
                OR vw_pc.idProfessorSupervisor = :idProfessor
            )
            and cast( vw_pc.dataInicial as date ) >= cast( :dataInicial as date )
                and cast( vw_pc.dataFinal as date ) <= cast( :dataFinal as date )"""
    )
    List<ProfessorDisponibilidadeProjection> getByProfessorAndData(Long idProfessor, LocalDate dataInicial, LocalDate dataFinal);

    @Query(value = """
            SELECT vw_pc
            FROM VwProfessorCompromisso vw_pc
            WHERE (
                vw_pc.idProfessor in :idProfessorList
                OR vw_pc.idProfessorBanca in :idProfessorList
                OR vw_pc.idProfessorOrientador in :idProfessorList
                OR vw_pc.idProfessorSupervisor in :idProfessorList
            )
            and ((vw_pc.dataInicial >= :dataInicial and vw_pc.dataInicial < :dataFinal)
                or (vw_pc.dataFinal <= :dataFinal and vw_pc.dataFinal > :dataInicial))
            and (vw_pc.idAgendaApresentacao is null or vw_pc.idAgendaApresentacao <> :idAgenda)"""
    )
    List<VwProfessorCompromisso> getByProfessoresAndDataAndNotAgenda(List<Long> idProfessorList, LocalDateTime dataInicial, LocalDateTime dataFinal, Long idAgenda);

}
