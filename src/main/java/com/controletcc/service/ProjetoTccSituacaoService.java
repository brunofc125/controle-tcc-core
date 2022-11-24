package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ProjetoTccSituacao;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.ProjetoTccSituacaoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccSituacaoService {

    private final ProjetoTccSituacaoRepository projetoTccSituacaoRepository;

    public ProjetoTccSituacao insert(@NonNull Long idProjetoTcc, @NonNull TipoTcc tipoTcc) throws BusinessException {
        var projetoTccSituacao = new ProjetoTccSituacao();
        projetoTccSituacao.setIdProjetoTcc(idProjetoTcc);
        projetoTccSituacao.setTipoTcc(tipoTcc);
        projetoTccSituacao.setSituacaoTcc(SituacaoTcc.EM_ANDAMENTO);
        return projetoTccSituacaoRepository.save(projetoTccSituacao);
    }

    public ProjetoTccSituacao nextStep(@NonNull Long idProjetoTcc, @NonNull SituacaoTcc situacaoTcc) throws BusinessException {
        var situacaoAtual = projetoTccSituacaoRepository.getSituacaoAtualByIdProjetoTcc(idProjetoTcc);
        if (situacaoAtual.getSituacaoTcc().equals(situacaoTcc)) {
            throw new BusinessException("O TCC já está na situação " + situacaoTcc.getDescricao());
        }
        if (situacaoAtual.getSituacaoTcc().isSituacaoFinal()) {
            throw new BusinessException("O TCC está em uma situação final (" + situacaoAtual.getSituacaoTcc().getDescricao() + "), não é possível alterar para " + situacaoTcc.getDescricao());
        }
        situacaoAtual.setDataConclusao(LocalDateTime.now());
        projetoTccSituacaoRepository.save(situacaoAtual);
        var projetoTccSituacao = new ProjetoTccSituacao();
        projetoTccSituacao.setIdProjetoTcc(idProjetoTcc);
        projetoTccSituacao.setTipoTcc(situacaoAtual.getTipoTcc());
        projetoTccSituacao.setSituacaoTcc(situacaoTcc);
        if (situacaoTcc.isSituacaoFinal()) {
            projetoTccSituacao.setDataConclusao(LocalDateTime.now());
        }
        return projetoTccSituacaoRepository.save(projetoTccSituacao);
    }

    public ProjetoTccSituacao nextStep(@NonNull Long idProjetoTcc, @NonNull TipoTcc tipoTcc) throws BusinessException {
        var situacaoAtual = projetoTccSituacaoRepository.getSituacaoAtualByIdProjetoTcc(idProjetoTcc);
        if (TipoTcc.DEFESA.equals(situacaoAtual.getTipoTcc())) {
            throw new BusinessException("Não é possível alterar o tipo do TCC para " + TipoTcc.DEFESA.getDescricao() + ", pois este já se encontra em sua fase final");
        } else if (TipoTcc.QUALIFICACAO.equals(tipoTcc)) {
            throw new BusinessException("Não é possível alterar o tipo do TCC para " + TipoTcc.QUALIFICACAO.getDescricao() + ", pois este já passou desta fase");
        } else if (SituacaoTcc.APROVADO.equals(situacaoAtual.getSituacaoTcc())) {
            throw new BusinessException("Não é possível alterar o tipo do TCC para " + tipoTcc.getDescricao() + ", pois sua situação atual está " + situacaoAtual.getSituacaoTcc().getDescricao() + " e deveria ser " + SituacaoTcc.APROVADO.getDescricao());
        }
        if (situacaoAtual.getDataConclusao() == null) {
            situacaoAtual.setDataConclusao(LocalDateTime.now());
        }
        projetoTccSituacaoRepository.save(situacaoAtual);
        var projetoTccSituacao = new ProjetoTccSituacao();
        projetoTccSituacao.setIdProjetoTcc(idProjetoTcc);
        projetoTccSituacao.setTipoTcc(tipoTcc);
        projetoTccSituacao.setSituacaoTcc(SituacaoTcc.EM_ANDAMENTO);
        return projetoTccSituacaoRepository.save(projetoTccSituacao);
    }

}
