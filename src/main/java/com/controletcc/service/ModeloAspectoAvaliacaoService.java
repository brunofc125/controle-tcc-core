package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloAspectoAvaliacao;
import com.controletcc.repository.ModeloAspectoAvaliacaoRepository;
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
public class ModeloAspectoAvaliacaoService {

    private final ModeloAspectoAvaliacaoRepository modeloAspectoAvaliacaoRepository;

    public List<ModeloAspectoAvaliacao> saveAll(@NonNull Long idModeloItemAvaliacao, List<ModeloAspectoAvaliacao> modeloAspectos) throws BusinessException {
        validate(modeloAspectos);
        var modeloAspectosSalvos = modeloAspectoAvaliacaoRepository.getAllByModeloItemAvaliacaoId(idModeloItemAvaliacao);
        var modeloAspectosDelete = modeloAspectosSalvos.stream().filter(m -> modeloAspectos.stream().noneMatch(ma -> m.getId().equals(ma.getId()))).toList();
        if (!modeloAspectosDelete.isEmpty()) {
            modeloAspectoAvaliacaoRepository.deleteAll(modeloAspectosDelete);
        }
        return modeloAspectoAvaliacaoRepository.saveAll(modeloAspectos);
    }

    private void validate(List<ModeloAspectoAvaliacao> modeloAspectos) throws BusinessException {
        var errors = new ArrayList<String>();

        if (modeloAspectos == null || modeloAspectos.isEmpty()) {
            errors.add("Deve ser informado pelo menos um item de avaliação");
        } else {
            for (var aspecto : modeloAspectos) {
                if (aspecto.getIdModeloItemAvaliacao() == null) {
                    errors.add("Existe aspecto sem vínculo com o modelo de avaliação");
                    break;
                }
                if (StringUtil.isNullOrBlank(aspecto.getDescricao())) {
                    errors.add("Existe aspecto sem a descrição");
                    break;
                } else if (modeloAspectos.stream().anyMatch(a -> modeloAspectos.indexOf(aspecto) != modeloAspectos.indexOf(a) && StringUtil.equalsTrimIgnoreCase(aspecto.getDescricao(), a.getDescricao()))) {
                    errors.add("Existe aspectos com a descrição duplicada");
                    break;
                }
                if (aspecto.getPeso() == null) {
                    errors.add("Existe item sem o peso");
                    break;
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

}
