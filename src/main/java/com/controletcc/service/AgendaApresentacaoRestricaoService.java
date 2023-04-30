package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.AgendaApresentacao;
import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import com.controletcc.repository.AgendaApresentacaoRestricaoRepository;
import com.controletcc.util.EventTimeUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AgendaApresentacaoRestricaoService {

    private final AgendaApresentacaoRestricaoRepository agendaApresentacaoRestricaoRepository;

    public List<AgendaApresentacaoRestricao> saveAll(@NonNull AgendaApresentacao agendaApresentacao, List<AgendaApresentacaoRestricao> restricoes) throws BusinessException {
        validate(agendaApresentacao, restricoes);
        var restricoesSalvas = agendaApresentacaoRestricaoRepository.getAllByAgendaApresentacaoId(agendaApresentacao.getId());
        var restricoesDelete = restricoesSalvas.stream().filter(rs -> restricoes.stream().noneMatch(r -> rs.getId().equals(r.getId()))).toList();
        if (!restricoesDelete.isEmpty()) {
            agendaApresentacaoRestricaoRepository.deleteAll(restricoesDelete);
        }
        return agendaApresentacaoRestricaoRepository.saveAll(restricoes);
    }

    private void validate(@NonNull AgendaApresentacao agendaApresentacao, @NonNull List<AgendaApresentacaoRestricao> restricoes) throws BusinessException {
        var errors = new ArrayList<String>();

        if (!restricoes.isEmpty()) {
            if (restricoes.stream().anyMatch(r -> r.getAgendaApresentacao() == null)) {
                errors.add("Existem restrições sem a agenda de apresentação definida");
            }

            if (EventTimeUtil.isDataInicialEmpty(restricoes)) {
                errors.add("Existem restrições sem a data inicial definida");
            }

            if (EventTimeUtil.isDataFinalEmpty(restricoes)) {
                errors.add("Existem restrições sem a data final definida");
            }

            if (EventTimeUtil.isDataInicialEqualOrAfterDataFinal(restricoes)) {
                errors.add("Existem restrições com a data inicial maior ou igual a data final");
            }

            if (EventTimeUtil.isDataInicialAndDataFinalDifferentDays(restricoes)) {
                errors.add("Existem restrições com intervalo de dias diferentes");
            }

            if (EventTimeUtil.isHourInvalid(restricoes)) {
                errors.add("Existem restrições com horas não múltiplas de 1 hora");
            }

            if (EventTimeUtil.invalidInterval(restricoes, agendaApresentacao.getDataInicial(), agendaApresentacao.getDataFinal(), agendaApresentacao.getHoraInicial(), agendaApresentacao.getHoraFinal())) {
                errors.add("Existem restrições fora dos limites definidos na agenda");
            }

            if (EventTimeUtil.invalidInterpolation(restricoes)) {
                errors.add("Existem restrições que se interpolam");
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public List<AgendaApresentacaoRestricao> getAllByAnoPeriodoAndAreasTcc(Integer ano, Integer periodo, List<Long> idAreaTccList) {
        return agendaApresentacaoRestricaoRepository.getAllByAnoPeriodoAndAreasTcc(ano, periodo, idAreaTccList);
    }

}
