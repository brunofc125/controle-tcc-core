package com.controletcc.service;

import com.controletcc.dto.ModeloAvaliacaoResumeDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloAvaliacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloAvaliacao;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.ModeloAvaliacaoRepository;
import com.controletcc.repository.projection.ModeloAvaliacaoProjection;
import com.controletcc.repository.projection.ModeloAvaliacaoValidateProjection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        var page = modeloAvaliacaoRepository.search(idAreaTccList, options.getId(), options.getIdAreaTcc(), options.getTipoTcc(), options.getTipoProfessor(), options.getPageable());
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

    private void validate(ModeloAvaliacao modeloAvaliacao) throws BusinessException {
        var errors = new ArrayList<String>();

        if (modeloAvaliacao.getIdAreaTcc() == null) {
            errors.add("Área de TCC não informada");
        }

        if (modeloAvaliacao.getTipoTccs() == null || modeloAvaliacao.getTipoTccs().isEmpty()) {
            errors.add("Tipo de TCC não informado");
        }

        if (modeloAvaliacao.getTipoProfessores() == null || modeloAvaliacao.getTipoProfessores().isEmpty()) {
            errors.add("Tipo de Professor não informado");
        }

        if (modeloAvaliacao.getModeloItensAvaliacao() == null || modeloAvaliacao.getModeloItensAvaliacao().isEmpty()) {
            errors.add("Deve ser informado pelo menos um item de avaliação");
        }

        if (errors.isEmpty()) {
            var modeloAvaliacaoResumeList = getValidate(modeloAvaliacao.getIdAreaTcc(), modeloAvaliacao.getTipoTccs(), modeloAvaliacao.getTipoProfessores(), modeloAvaliacao.getId());
            if (!modeloAvaliacaoResumeList.isEmpty()) {
                var errorsDuplicate = modeloAvaliacaoResumeList.stream().map(m -> {
                    var prefixo = m.getTipoTccs().stream().map(TipoTcc::getDescricao).collect(Collectors.joining(", "));
                    var sufixo = m.getTipoProfessores().stream().map(TipoProfessor::getDescricao).collect(Collectors.joining(", "));
                    return "ID: " + m.getId() + " - " + prefixo + " com " + sufixo;
                }).toList();

                if (!errorsDuplicate.isEmpty()) {
                    var title = errorsDuplicate.size() > 1 ?
                            "Já existem modelos de avaliação para esta área com esta configuração:" :
                            "Já existe um modelo de avaliação para esta área com esta configuração:";
                    throw new BusinessException(title, errorsDuplicate);
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    private List<ModeloAvaliacaoResumeDTO> getValidate(Long idAreaTcc, Set<TipoTcc> tipoTccs, Set<TipoProfessor> tipoProfessores, Long id) {
        var modelos = modeloAvaliacaoRepository.getValidateByAreaTccAndTipoTccsAndTipoProfessoresAndNotId(idAreaTcc, tipoTccs, tipoProfessores, id);
        return modelos.isEmpty() ?
                Collections.emptyList() :
                modelos.stream()
                        .collect(Collectors.groupingBy(ModeloAvaliacaoValidateProjection::getId))
                        .entrySet()
                        .stream()
                        .map(entry -> {
                            ModeloAvaliacaoResumeDTO dto = new ModeloAvaliacaoResumeDTO();
                            dto.setId(entry.getKey());
                            dto.setTipoTccs(entry.getValue().stream().map(ModeloAvaliacaoValidateProjection::getTipoTcc).collect(Collectors.toSet()));
                            dto.setTipoProfessores(entry.getValue().stream().map(ModeloAvaliacaoValidateProjection::getTipoProfessor).collect(Collectors.toSet()));
                            return dto;
                        }).toList();
    }

}
