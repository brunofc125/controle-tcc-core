package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloItemAvaliacao;
import com.controletcc.repository.ModeloItemAvaliacaoRepository;
import com.controletcc.util.StringUtil;
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
public class ModeloItemAvaliacaoService {

    private final ModeloItemAvaliacaoRepository modeloItemAvaliacaoRepository;

    public List<ModeloItemAvaliacao> saveAll(@NonNull Long idModeloAvaliacao, List<ModeloItemAvaliacao> modeloItens) throws BusinessException {
        validate(modeloItens);
        var modeloItensSalvos = modeloItemAvaliacaoRepository.getAllByModeloAvaliacaoId(idModeloAvaliacao);
        var modeloItensDelete = modeloItensSalvos.stream().filter(mis -> modeloItens.stream().noneMatch(mi -> mis.getId().equals(mi.getId()))).toList();
        if (!modeloItensDelete.isEmpty()) {
            modeloItemAvaliacaoRepository.deleteAll(modeloItensDelete);
        }
        return modeloItemAvaliacaoRepository.saveAll(modeloItens);
    }

    private void validate(List<ModeloItemAvaliacao> modeloItens) throws BusinessException {
        var errors = new ArrayList<String>();

        if (modeloItens == null || modeloItens.isEmpty()) {
            errors.add("Deve ser informado pelo menos um item de avaliação");
        } else {
            for (var item : modeloItens) {
                if (item.getIdModeloAvaliacao() == null) {
                    errors.add("Existe item sem vínculo com o modelo de avaliação");
                    break;
                }
                if (StringUtil.isNullOrBlank(item.getDescricao())) {
                    errors.add("Existe item sem a descrição");
                    break;
                } else if (modeloItens.stream().anyMatch(i -> modeloItens.indexOf(item) != modeloItens.indexOf(i) && StringUtil.equalsTrimIgnoreCase(item.getDescricao(), i.getDescricao()))) {
                    errors.add("Existe itens com a descrição duplicada");
                    break;
                }
                if (item.getPeso() == null) {
                    errors.add("Existe item sem o peso");
                    break;
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    private boolean validDescricaoDuplicate(Long id, Long idModeloAvaliacao, String descricao) {
        return id != null ?
                modeloItemAvaliacaoRepository.existsByModeloAvaliacaoIdAndDescricaoIgnoreCaseAndIdNot(idModeloAvaliacao, descricao, id)
                : modeloItemAvaliacaoRepository.existsByModeloAvaliacaoIdAndDescricaoIgnoreCase(idModeloAvaliacao, descricao);
    }

}
