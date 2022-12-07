package com.controletcc.repository;

import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaApresentacaoRestricaoRepository extends JpaRepository<AgendaApresentacaoRestricao, Long> {

    List<AgendaApresentacaoRestricao> getAllByAgendaApresentacaoId(Long idAgendaApresentacao);

}
