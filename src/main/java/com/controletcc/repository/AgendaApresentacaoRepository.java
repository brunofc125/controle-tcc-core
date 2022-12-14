package com.controletcc.repository;

import com.controletcc.model.entity.AgendaApresentacao;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.AgendaApresentacaoProjection;
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
                aa.dataInicial as dataInicial,
                aa.dataFinal as dataFinal
            FROM AgendaApresentacao aa
            JOIN aa.areaTcc at
                where at.id in :idAreaTccList
                and (:id is null or aa.id = :id)
                and (:descricao is null or lower(aa.descricao) like concat('%', trim(lower(:descricao)),'%') )
                and (:tipoTcc is null or aa.tipoTcc = :tipoTcc)
                and (:idAreaTcc is null or at.id = :idAreaTcc)
                and (cast( :dataInicial as date ) is null or aa.dataInicial >= cast( :dataInicial as date ) )
                and (cast( :dataFinal as date ) is null or aa.dataFinal <= cast( :dataFinal as date ) )"""
    )
    Page<AgendaApresentacaoProjection> search(Long id,
                                              List<Long> idAreaTccList,
                                              String descricao,
                                              TipoTcc tipoTcc,
                                              Long idAreaTcc,
                                              LocalDate dataInicial,
                                              LocalDate dataFinal,
                                              Pageable pageable);

    @Query(value = """
            SELECT count(aa.id) > 0
            FROM AgendaApresentacao aa
            WHERE aa.areaTcc.id = :idAreaTcc
            AND aa.tipoTcc = :tipoTcc
            AND (:id is null OR aa.id <> :id)
            AND (
                (:dataInicial >= aa.dataInicial AND :dataInicial <= aa.dataFinal) OR (aa.dataInicial >= :dataInicial AND aa.dataInicial <= :dataFinal)
                OR (:dataFinal >= aa.dataInicial AND :dataFinal <= aa.dataFinal) OR (aa.dataFinal >= :dataInicial AND aa.dataFinal <= :dataFinal)
            )"""
    )
    boolean existsIntersect(Long id, Long idAreaTcc, TipoTcc tipoTcc, LocalDate dataInicial, LocalDate dataFinal);

    List<AgendaApresentacao> getAllByTipoTccAndAreaTccIdAndDataFinalGreaterThanEqual(TipoTcc tipoTcc, Long idAreaTcc, LocalDate data);

    List<AgendaApresentacao> getAllByAreaTccIdInAndDataFinalGreaterThanEqual(List<Long> idAreaTccList, LocalDate data);

}
