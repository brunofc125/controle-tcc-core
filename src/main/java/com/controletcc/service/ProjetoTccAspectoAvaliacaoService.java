package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloAspectoAvaliacao;
import com.controletcc.model.entity.ProjetoTccAspectoAvaliacao;
import com.controletcc.model.entity.ProjetoTccAvaliacao;
import com.controletcc.repository.ProjetoTccAspectoAvaliacaoRepository;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccAspectoAvaliacaoService {

    private final ProjetoTccAspectoAvaliacaoRepository projetoTccAspectoAvaliacaoRepository;

    public List<ProjetoTccAspectoAvaliacao> generateByList(@NonNull ProjetoTccAvaliacao projetoTccAvaliacao, List<ModeloAspectoAvaliacao> aspectos) {
        return aspectos != null && !aspectos.isEmpty() ? aspectos.stream().map(a -> generate(projetoTccAvaliacao, a)).toList() : Collections.emptyList();
    }

    private ProjetoTccAspectoAvaliacao generate(@NonNull ProjetoTccAvaliacao projetoTccAvaliacao, @NonNull ModeloAspectoAvaliacao modeloAspectoAvaliacao) {
        var projetoTccAspectoAvaliacao = new ProjetoTccAspectoAvaliacao();
        projetoTccAspectoAvaliacao.setProjetoTccAvaliacao(projetoTccAvaliacao);
        projetoTccAspectoAvaliacao.setDescricao(modeloAspectoAvaliacao.getDescricao());
        projetoTccAspectoAvaliacao.setPeso(modeloAspectoAvaliacao.getPeso());
        return projetoTccAspectoAvaliacaoRepository.save(projetoTccAspectoAvaliacao);
    }

    public List<ProjetoTccAspectoAvaliacao> saveAll(@NonNull Long idProjetoTccAvaliacao, List<ProjetoTccAspectoAvaliacao> aspectos) throws BusinessException {
        validate(idProjetoTccAvaliacao, aspectos);
        var aspectosSalvos = projetoTccAspectoAvaliacaoRepository.getAllByProjetoTccAvaliacaoId(idProjetoTccAvaliacao);
        var aspectosDelete = aspectosSalvos.stream().filter(m -> aspectos.stream().noneMatch(ma -> m.getId().equals(ma.getId()))).toList();
        if (!aspectosDelete.isEmpty()) {
            projetoTccAspectoAvaliacaoRepository.deleteAll(aspectosDelete);
        }
        return projetoTccAspectoAvaliacaoRepository.saveAll(aspectos);
    }

    private void validate(Long idProjetoTccAvaliacao, List<ProjetoTccAspectoAvaliacao> aspectos) throws BusinessException {
        var errors = new ArrayList<String>();

        if (aspectos == null || aspectos.isEmpty()) {
            errors.add("Deve ser informado pelo menos um aspecto de avaliação");
        } else {
            for (var aspecto : aspectos) {
                if (aspecto.getIdProjetoTccAvaliacao() == null) {
                    errors.add("Existe aspecto sem vínculo com avaliação");
                    break;
                } else if (!aspecto.getIdProjetoTccAvaliacao().equals(idProjetoTccAvaliacao)) {
                    errors.add("Avaliação inválida definida no aspecto");
                    break;
                }
                if (StringUtil.isNullOrBlank(aspecto.getDescricao())) {
                    errors.add("Existe aspecto sem a descrição");
                    break;
                } else if (aspectos.stream().anyMatch(a -> aspectos.indexOf(aspecto) != aspectos.indexOf(a) && StringUtil.equalsTrimIgnoreCase(aspecto.getDescricao(), a.getDescricao()))) {
                    errors.add("Existe aspectos com a descrição duplicada");
                    break;
                }
                if (aspecto.getPeso() == null) {
                    errors.add("Existe aspecto sem o peso");
                    break;
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public List<ProjetoTccAspectoAvaliacao> getAllByIdList(List<Long> idList) {
        return projetoTccAspectoAvaliacaoRepository.getAllByIdIn(idList);
    }

    public boolean isValidoLancamentoNota(Long idProjetoTcc) {
        var qtdAvaliacao = projetoTccAspectoAvaliacaoRepository.countByProjetoTcc(idProjetoTcc);
        var qtdAvaliacaoNula = projetoTccAspectoAvaliacaoRepository.countValorNuloByProjetoTcc(idProjetoTcc);
        return qtdAvaliacao > 0 && qtdAvaliacaoNula == 0;
    }

    public List<ProjetoTccAspectoAvaliacao> getAllByProjetoTcc(Long idProjetoTcc) {
        return projetoTccAspectoAvaliacaoRepository.getAllByProjetoTcc(idProjetoTcc);
    }

    public Double calculateValorFinal(List<ProjetoTccAspectoAvaliacao> aspectos) {
        var pesoTotal = Double.valueOf(0);
        var notaFinal = Double.valueOf(0);
        for (var aspecto : aspectos) {
            if (aspecto.getPeso() != null) {
                pesoTotal += aspecto.getPeso();
                var valor = aspecto.getValor() != null ? aspecto.getValor() : 0d;
                notaFinal += aspecto.getPeso() * valor;
            }
        }
        return pesoTotal > 0 ? notaFinal / pesoTotal : 0d;
    }

}
