package com.controletcc.repository;

import com.controletcc.model.entity.AgendaApresentacao;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.AgendaApresentacaoProjection;
import com.controletcc.repository.projection.AgendaPeriodoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AgendaApresentacaoRepository extends JpaRepository<AgendaApresentacao, Long> {

    @Query(value = """  
            SELECT
                aa.id as id,
                aa.descricao as descricao,
                aa.tipoTcc as tipoTcc,
                concat(at.faculdade, ' - ', at.curso) as descricaoAreaTcc,
                concat(aa.ano, '/', aa.periodo) as anoPeriodo,
                aa.dataInicial as dataInicial,
                aa.dataFinal as dataFinal
            FROM AgendaApresentacao aa
            JOIN aa.areaTcc at
                where at.id in :idAreaTccList
                and (:id is null or aa.id = :id)
                and (:descricao is null or lower(aa.descricao) like concat('%', trim(lower(:descricao)),'%') )
                and (:tipoTcc is null or aa.tipoTcc = :tipoTcc)
                and (:idAreaTcc is null or at.id = :idAreaTcc)
                and (:anoPeriodo is null or concat(aa.ano, '/', aa.periodo) like concat('%', trim(lower(:anoPeriodo)),'%') )
                and (cast( :dataInicial as date ) is null or aa.dataInicial >= cast( :dataInicial as date ) )
                and (cast( :dataFinal as date ) is null or aa.dataFinal <= cast( :dataFinal as date ) )"""
    )
    Page<AgendaApresentacaoProjection> search(Long id,
                                              List<Long> idAreaTccList,
                                              String descricao,
                                              TipoTcc tipoTcc,
                                              Long idAreaTcc,
                                              String anoPeriodo,
                                              LocalDate dataInicial,
                                              LocalDate dataFinal,
                                              Pageable pageable);

    boolean existsByAreaTccIdAndTipoTccAndAnoAndPeriodo(Long idAreaTcc, TipoTcc tipoTcc, Integer ano, Integer periodo);

    boolean existsByAreaTccIdAndTipoTccAndAnoAndPeriodoAndIdNot(Long idAreaTcc, TipoTcc tipoTcc, Integer ano, Integer periodo, Long id);

    List<AgendaApresentacao> getAllByTipoTccAndAreaTccIdAndDataFinalGreaterThanEqual(TipoTcc tipoTcc, Long idAreaTcc, LocalDate data);

    List<AgendaApresentacao> getAllByAreaTccIdInAndDataFinalGreaterThanEqual(List<Long> idAreaTccList, LocalDate data);

    List<AgendaApresentacao> getAllByAnoAndPeriodo(Integer ano, Integer periodo);


    @Query(value = """
            select
                aa.ano as ano,
                aa.periodo as periodo,
                min(aa.dataInicial) as minDataInicial,
                max(aa.dataFinal) as maxDataFinal,
                min(aa.horaInicial) as minHoraInicial,
                max(aa.horaFinal) as maxHoraFinal
            from AgendaApresentacao aa
            where aa.ano = :ano
            and aa.periodo = :periodo
            and aa.areaTcc.id in :idAreaTccList
            group by aa.ano, aa.periodo"""
    )
    AgendaPeriodoProjection getAgendaByAnoPeriodoAndAreasTcc(Integer ano, Integer periodo, List<Long> idAreaTccList);

}
