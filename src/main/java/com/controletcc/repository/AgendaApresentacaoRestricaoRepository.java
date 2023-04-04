package com.controletcc.repository;

import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgendaApresentacaoRestricaoRepository extends JpaRepository<AgendaApresentacaoRestricao, Long> {

    List<AgendaApresentacaoRestricao> getAllByAgendaApresentacaoId(Long idAgendaApresentacao);

    @Query(value = """
            select aar
            from AgendaApresentacaoRestricao aar
            join aar.agendaApresentacao aa
            where aa.ano = :ano
                and aa.periodo = :periodo
                and aa.areaTcc.id in :idAreaTccList"""
    )
    List<AgendaApresentacaoRestricao> getAllByAnoPeriodoAndAreasTcc(Integer ano, Integer periodo, List<Long> idAreaTccList);

}
