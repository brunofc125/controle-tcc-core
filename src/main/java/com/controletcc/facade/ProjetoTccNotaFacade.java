package com.controletcc.facade;

import com.controletcc.dto.ProjetoTccNotaResumeDTO;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProjetoTccNotaDTO;
import com.controletcc.model.entity.ProjetoTccAspectoAvaliacao;
import com.controletcc.model.entity.ProjetoTccSituacao;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.service.ProjetoTccAspectoAvaliacaoService;
import com.controletcc.service.ProjetoTccNotaService;
import com.controletcc.service.ProjetoTccService;
import com.controletcc.service.ProjetoTccSituacaoService;
import com.controletcc.util.DoubleUtil;
import com.controletcc.util.ModelMapperUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccNotaFacade {

    private final ProjetoTccNotaService projetoTccNotaService;

    private final ProjetoTccAspectoAvaliacaoService projetoTccAspectoAvaliacaoService;

    private final ProjetoTccSituacaoService projetoTccSituacaoService;

    private final ProjetoTccService projetoTccService;

    public ProjetoTccNotaDTO getByProjetoTcc(@NonNull Long idProjetoTcc) {
        return ModelMapperUtil.map(projetoTccNotaService.getByProjetoTcc(idProjetoTcc), ProjetoTccNotaDTO.class);
    }

    public ProjetoTccNotaDTO lancarNota(@NonNull Long idProjetoTcc) throws Exception {
        if (!projetoTccAspectoAvaliacaoService.isValidoLancamentoNota(idProjetoTcc)) {
            throw new BusinessException("Existem avaliações não finalizadas");
        }
        Double notaFinal = calculateNotaFinal(idProjetoTcc);
        var projetoTccNota = projetoTccNotaService.updateNotaFinal(idProjetoTcc, notaFinal);
        ProjetoTccSituacao situacaoNova = projetoTccNota.getNotaMedia().compareTo(notaFinal) <= 0 ?
                projetoTccSituacaoService.nextStep(idProjetoTcc, SituacaoTcc.APROVADO, "Nota acima da média")
                : projetoTccSituacaoService.nextStep(idProjetoTcc, SituacaoTcc.REPROVADO, "Nota abaixo da média");
        projetoTccService.updateSituacao(idProjetoTcc, situacaoNova);
        return ModelMapperUtil.map(projetoTccNota, ProjetoTccNotaDTO.class);
    }

    public ProjetoTccNotaResumeDTO getNotaCalculada(@NonNull Long idProjetoTcc) throws Exception {
        var nota = ModelMapperUtil.map(projetoTccNotaService.getByProjetoTcc(idProjetoTcc), ProjetoTccNotaResumeDTO.class);
        if (nota == null) {
            return null;
        } else if (nota.getNotaFinal() != null) {
            nota.setAvaliacaoFinalizada(true);
            return nota;
        }
        nota.setAvaliacaoFinalizada(projetoTccAspectoAvaliacaoService.isValidoLancamentoNota(idProjetoTcc));
        if (nota.isAvaliacaoFinalizada()) {
            nota.setNotaFinal(calculateNotaFinal(idProjetoTcc));
        }
        return nota;
    }

    private Double calculateNotaFinal(@NonNull Long idProjetoTcc) {
        var aspectos = projetoTccAspectoAvaliacaoService.getAllByProjetoTcc(idProjetoTcc);
        var aspectosMapByAvaliacao = aspectos.stream().collect(Collectors.groupingBy(ProjetoTccAspectoAvaliacao::getIdProjetoTccAvaliacao));
        var notas = new ArrayList<Double>();
        for (var aspectoEntrySet : aspectosMapByAvaliacao.entrySet()) {
            notas.add(projetoTccAspectoAvaliacaoService.calculateValorFinal(aspectoEntrySet.getValue()));
        }
        var notaFinal = Double.valueOf(0);
        for (var nota : notas) {
            notaFinal += nota;
        }
        notaFinal = DoubleUtil.roundingHalfUp(2, notaFinal / notas.size());
        return notaFinal;
    }
    
}
