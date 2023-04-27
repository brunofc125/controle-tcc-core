package com.controletcc.facade;

import com.controletcc.dto.AgendaPeriodoDTO;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ApresentacaoDTO;
import com.controletcc.model.entity.Apresentacao;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.service.AgendaApresentacaoService;
import com.controletcc.service.ApresentacaoService;
import com.controletcc.service.ProfessorService;
import com.controletcc.service.ProjetoTccService;
import com.controletcc.util.ModelMapperUtil;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ApresentacaoFacade {

    private final ApresentacaoService apresentacaoService;

    private final ProjetoTccService projetoTccService;

    private final AgendaApresentacaoService agendaApresentacaoService;

    private final ProjetoTccSituacaoFacade projetoTccSituacaoFacade;

    private final ProfessorService professorService;

    public ApresentacaoDTO getByProjetoTcc(@NonNull Long idProjetoTcc) {
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var apresentacao = apresentacaoService.getFirstByProjetoTccIdAndTipoTcc(idProjetoTcc, projetoTcc.getTipoTcc());
        return apresentacao != null ? ModelMapperUtil.map(apresentacao, ApresentacaoDTO.class) : null;
    }

    public ApresentacaoDTO insert(ApresentacaoDTO apresentacaoDTO) throws BusinessException {
        var apresentacao = buildApresentacao(apresentacaoDTO);
        apresentacao = apresentacaoService.insert(apresentacao);
        projetoTccSituacaoFacade.nextStep(apresentacao.getIdProjetoTcc(), SituacaoTcc.A_APRESENTAR);
        return ModelMapperUtil.map(apresentacao, ApresentacaoDTO.class);
    }

    public ApresentacaoDTO update(ApresentacaoDTO apresentacaoDTO) throws BusinessException {
        var apresentacao = buildApresentacao(apresentacaoDTO);
        apresentacao = apresentacaoService.update(apresentacao.getId(), apresentacao);
        return ModelMapperUtil.map(apresentacao, ApresentacaoDTO.class);
    }

    private Apresentacao buildApresentacao(ApresentacaoDTO apresentacaoDTO) {
        var apresentacao = ModelMapperUtil.map(apresentacaoDTO, Apresentacao.class);
        if (apresentacao.getIdProjetoTcc() != null && apresentacao.getAgendaApresentacao() != null) {
            var projetoTcc = projetoTccService.getById(apresentacao.getIdProjetoTcc());
            var agendaApresentacao = agendaApresentacaoService.getById(apresentacao.getIdAgendaApresentacao());
            apresentacao.setProjetoTcc(projetoTcc);
            apresentacao.setAgendaApresentacao(agendaApresentacao);
            apresentacao.setTipoTcc(projetoTcc.getTipoTcc());
        }
        return apresentacao;
    }

    public AgendaPeriodoDTO getAllByProfessorLogadoAndAnoPeriodo(String anoPeriodo) throws BusinessException {
        if (StringUtil.isNullOrBlank(anoPeriodo) || !anoPeriodo.matches("\\d{4}-\\d")) {
            return null;
        }
        var professor = professorService.getProfessorLogado();
        var ano = Integer.valueOf(anoPeriodo.substring(0, 4));
        var periodo = Integer.valueOf(anoPeriodo.substring(5));
        var agendaPeriodoProjection = agendaApresentacaoService.getAgendaPeriodoByAnoPeriodoAndAreasTcc(ano, periodo, professor.getIdAreaList());
        if (agendaPeriodoProjection == null) {
            return null;
        }
        var agendaPeriodo = new AgendaPeriodoDTO(agendaPeriodoProjection);
        agendaPeriodo.setApresentacoes(apresentacaoService.getAllByProfessorAndAnoPeriodo(professor.getId(), ano, periodo));
        return agendaPeriodo;
    }

}

