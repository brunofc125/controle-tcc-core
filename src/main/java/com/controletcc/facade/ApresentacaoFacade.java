package com.controletcc.facade;

import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ApresentacaoDTO;
import com.controletcc.model.entity.Apresentacao;
import com.controletcc.service.AgendaApresentacaoService;
import com.controletcc.service.ApresentacaoService;
import com.controletcc.service.ProjetoTccService;
import com.controletcc.util.ModelMapperUtil;
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

    public ApresentacaoDTO getByProjetoTcc(@NonNull Long idProjetoTcc) {
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var apresentacao = apresentacaoService.getFirstByProjetoTccIdAndTipoTcc(idProjetoTcc, projetoTcc.getTipoTcc());
        return apresentacao != null ? ModelMapperUtil.map(apresentacaoService.getFirstByProjetoTccIdAndTipoTcc(idProjetoTcc, projetoTcc.getTipoTcc()), ApresentacaoDTO.class) : null;
    }

    public ApresentacaoDTO insert(ApresentacaoDTO apresentacaoDTO) throws BusinessException {
        var apresentacao = buildApresentacao(apresentacaoDTO);
        apresentacao = apresentacaoService.insert(apresentacao);
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


}

