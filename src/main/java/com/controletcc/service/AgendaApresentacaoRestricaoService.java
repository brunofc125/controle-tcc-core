package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import com.controletcc.repository.AgendaApresentacaoRestricaoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AgendaApresentacaoRestricaoService {

    private final AgendaApresentacaoRestricaoRepository agendaApresentacaoRestricaoRepository;

    public List<AgendaApresentacaoRestricao> saveAll(@NonNull Long idAgendaApresentacao, List<AgendaApresentacaoRestricao> restricoes) {
        var restricoesSalvas = agendaApresentacaoRestricaoRepository.getAllByAgendaApresentacaoId(idAgendaApresentacao);
        var restricoesDelete = restricoesSalvas.stream().filter(rs -> restricoes.stream().noneMatch(r -> rs.getId().equals(r.getId()))).toList();
        if (!restricoesDelete.isEmpty()) {
            agendaApresentacaoRestricaoRepository.deleteAll(restricoesDelete);
        }
        return agendaApresentacaoRestricaoRepository.saveAll(restricoes);
    }

}
