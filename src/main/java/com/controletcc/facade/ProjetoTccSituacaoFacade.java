package com.controletcc.facade;

import com.controletcc.error.BusinessException;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.service.ProjetoTccService;
import com.controletcc.service.ProjetoTccSituacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccSituacaoFacade {

    private final ProjetoTccSituacaoService projetoTccSituacaoService;

    private final ProjetoTccService projetoTccService;

    public void nextStep(Long idProjetoTcc, SituacaoTcc situacaoTcc) throws BusinessException {
        nextStep(idProjetoTcc, situacaoTcc, null);
    }

    public void nextStep(Long idProjetoTcc, SituacaoTcc situacaoTcc, String motivo) throws BusinessException {
        var situacao = projetoTccSituacaoService.nextStep(idProjetoTcc, situacaoTcc, motivo);
        projetoTccService.updateSituacao(idProjetoTcc, situacao);
    }

    public void toDefesa(Long idProjetoTcc) throws BusinessException {
        var situacao = projetoTccSituacaoService.toDefesa(idProjetoTcc);
        projetoTccService.updateSituacao(idProjetoTcc, situacao);
    }

}

