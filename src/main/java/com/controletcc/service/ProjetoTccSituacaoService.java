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

    public ProjetoTccSituacao nextStep(@NonNull Long idProjetoTcc, @NonNull SituacaoTcc situacaoTcc, String motivo) throws BusinessException {
        var situacaoAtual = projetoTccSituacaoRepository.getSituacaoAtualByIdProjetoTcc(idProjetoTcc);
        if (situacaoAtual.getSituacaoTcc().equals(situacaoTcc)) {
            throw new BusinessException("O TCC já está na situação " + situacaoTcc.getDescricao());
        }
        if (situacaoAtual.getSituacaoTcc().isSituacaoFinal()) {
            throw new BusinessException("O TCC está em uma situação final (" + situacaoAtual.getSituacaoTcc().getDescricao() + "), não é possível alterar para " + situacaoTcc.getDescricao());
        }
        if (!situacaoAtual.getSituacaoTcc().canUpdateTo(situacaoTcc)) {
            throw new BusinessException("Não é possível alterar a situação do TCC de " + situacaoAtual.getSituacaoTcc().getDescricao() + " para " + situacaoTcc.getDescricao());
        }
        situacaoAtual.setDataConclusao(LocalDateTime.now());
        projetoTccSituacaoRepository.save(situacaoAtual);
        var projetoTccSituacao = new ProjetoTccSituacao();
        projetoTccSituacao.setIdProjetoTcc(idProjetoTcc);
        projetoTccSituacao.setTipoTcc(situacaoAtual.getTipoTcc());
        projetoTccSituacao.setSituacaoTcc(situacaoTcc);
        projetoTccSituacao.setMotivo(motivo);
        if (situacaoTcc.isSituacaoFinal()) {
            projetoTccSituacao.setDataConclusao(LocalDateTime.now());
        }
        return projetoTccSituacaoRepository.save(projetoTccSituacao);
    }

    public ProjetoTccSituacao toDefesa(@NonNull Long idProjetoTcc) throws BusinessException {
        var situacaoAtual = projetoTccSituacaoRepository.getSituacaoAtualByIdProjetoTcc(idProjetoTcc);
        if (!TipoTcc.QUALIFICACAO.equals(situacaoAtual.getTipoTcc())) {
            throw new BusinessException("Não é possível mudar para etapa de defesa, pois o TCC não está na etapa de qualificação");
        } else if (!SituacaoTcc.APROVADO.equals(situacaoAtual.getSituacaoTcc())) {
            throw new BusinessException("Não é possível mudar para etapa de defesa, pois o TCC não está aprovado na etapa de qualificação");
        }
        if (situacaoAtual.getDataConclusao() == null) {
            situacaoAtual.setDataConclusao(LocalDateTime.now());
        }
        projetoTccSituacaoRepository.save(situacaoAtual);
        var projetoTccSituacao = new ProjetoTccSituacao();
        projetoTccSituacao.setIdProjetoTcc(idProjetoTcc);
        projetoTccSituacao.setTipoTcc(TipoTcc.DEFESA);
        projetoTccSituacao.setSituacaoTcc(SituacaoTcc.EM_ANDAMENTO);
        return projetoTccSituacaoRepository.save(projetoTccSituacao);
    }

    public ProjetoTccSituacao nextStep(@NonNull Long idProjetoTcc, @NonNull TipoTcc tipoTcc) throws BusinessException {
        var situacaoAtual = projetoTccSituacaoRepository.getSituacaoAtualByIdProjetoTcc(idProjetoTcc);
        if (TipoTcc.DEFESA.equals(situacaoAtual.getTipoTcc())) {
            throw new BusinessException("Não é possível alterar o tipo do TCC, pois este já se encontra em sua fase final");
        } else if (TipoTcc.QUALIFICACAO.equals(tipoTcc)) {
            throw new BusinessException("Não é possível alterar o tipo do TCC para " + TipoTcc.QUALIFICACAO.getDescricao());
        } else if (!SituacaoTcc.APROVADO.equals(situacaoAtual.getSituacaoTcc())) {
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
