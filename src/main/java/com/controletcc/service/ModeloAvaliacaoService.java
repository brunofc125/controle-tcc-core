package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloAvaliacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloAvaliacao;
import com.controletcc.repository.ModeloAvaliacaoRepository;
import com.controletcc.repository.projection.ModeloAvaliacaoProjection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ModeloAvaliacaoService {

    private final ModeloAvaliacaoRepository modeloAvaliacaoRepository;

    public ModeloAvaliacao getById(@NonNull Long id) {
        return modeloAvaliacaoRepository.getReferenceById(id);
    }

    public ListResponse<ModeloAvaliacaoProjection> search(@NonNull List<Long> idAreaTccList, ModeloAvaliacaoGridOptions options) throws BusinessException {
        var page = modeloAvaliacaoRepository.search(idAreaTccList, options.getId(), options.getIdAreaTcc(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public ModeloAvaliacao insert(@NonNull ModeloAvaliacao modeloAvaliacao) throws BusinessException {
        modeloAvaliacao.setId(null);
        validate(modeloAvaliacao);
        return modeloAvaliacaoRepository.save(modeloAvaliacao);
    }

    public ModeloAvaliacao update(@NonNull Long id, @NonNull ModeloAvaliacao modeloAvaliacao) throws BusinessException {
        modeloAvaliacao.setId(id);
        validate(modeloAvaliacao);
        return modeloAvaliacaoRepository.save(modeloAvaliacao);
    }

    public void deleteLogic(@NonNull Long id) throws Exception {
        var modeloAvaliacao = modeloAvaliacaoRepository.getReferenceById(id);
        if (modeloAvaliacao.getDataExclusao() != null) {
            throw new BusinessException("Modelo de avaliação já deletado");
        }
        modeloAvaliacao.setDataExclusao(LocalDateTime.now());
        modeloAvaliacaoRepository.save(modeloAvaliacao);
    }

    private void validate(ModeloAvaliacao modeloAvaliacao) throws BusinessException {
        var errors = new ArrayList<String>();

        if (modeloAvaliacao.getIdAreaTcc() == null) {
            errors.add("Área de TCC não informada");
        } else if (existsDuplicate(modeloAvaliacao)) {
            errors.add("Já existe um modelo de avaliação com esta área de TCC");
        }

        var notaMediaValid = true;
        if (modeloAvaliacao.getNotaMedia() == null) {
            errors.add("Valor da média não informada");
            notaMediaValid = false;
        } else if (modeloAvaliacao.getNotaMedia().compareTo(0d) <= 0) {
            errors.add("Valor da média não pode ser menor ou igual a zero");
            notaMediaValid = false;
        }

        var notaMaximaValid = true;
        if (modeloAvaliacao.getNotaMaxima() == null) {
            errors.add("Valor da nota máxima não informada");
            notaMaximaValid = false;
        } else if (modeloAvaliacao.getNotaMaxima().compareTo(0d) <= 0) {
            errors.add("Valor da nota máxima não pode ser menor ou igual a zero");
            notaMaximaValid = false;
        }

        if (notaMediaValid && notaMaximaValid && modeloAvaliacao.getNotaMedia().compareTo(modeloAvaliacao.getNotaMaxima()) >= 0) {
            errors.add("Valor da média não pode ser maior ou igual a nota máxima");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    private boolean existsDuplicate(@NonNull ModeloAvaliacao modeloAvaliacao) {
        return modeloAvaliacao.getId() != null ?
                modeloAvaliacaoRepository.existsByAreaTccIdAndDataExclusaoNullAndIdNot(modeloAvaliacao.getIdAreaTcc(), modeloAvaliacao.getId())
                : modeloAvaliacaoRepository.existsByAreaTccIdAndDataExclusaoNull(modeloAvaliacao.getIdAreaTcc());
    }

    public ModeloAvaliacao getByAreaTcc(Long idAreaTcc) {
        return modeloAvaliacaoRepository.getModeloAvaliacaoByAreaTccIdAndDataExclusaoNull(idAreaTcc);
    }

}
