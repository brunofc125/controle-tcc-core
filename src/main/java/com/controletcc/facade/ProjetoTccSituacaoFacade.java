package com.controletcc.facade;

import com.controletcc.error.BusinessException;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
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
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var situacaoAtual = projetoTcc.getSituacaoAtual();
        if (TipoTcc.DEFESA.equals(situacaoAtual.getTipoTcc())) {
            throw new BusinessException("Este TCC já se encontra em defesa");
        }
        if (!SituacaoTcc.APROVADO.equals(situacaoAtual.getSituacaoTcc())) {
            throw new BusinessException("Este TCC está " + situacaoAtual.getSituacaoTcc().getDescricao() + ", é necessário que esteja aprovado");
        }
        var situacao = projetoTccSituacaoService.toDefesa(idProjetoTcc);
        var ano = projetoTcc.getAno();
        var periodo = projetoTcc.getPeriodo();
        if (Integer.valueOf(2).equals(periodo)) {
            ano++;
            periodo = 1;
        } else {
            periodo++;
        }
        projetoTccService.updateSituacaoAndAnoPeriodo(idProjetoTcc, situacao, ano + "/" + periodo);
    }

}

