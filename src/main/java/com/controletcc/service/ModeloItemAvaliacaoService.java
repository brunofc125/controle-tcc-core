package com.controletcc.service;

import com.controletcc.dto.ModeloAvaliacaoResumeDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloItemAvaliacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloItemAvaliacao;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.ModeloItemAvaliacaoRepository;
import com.controletcc.repository.projection.ModeloAvaliacaoValidateProjection;
import com.controletcc.repository.projection.ModeloItemAvaliacaoProjection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ModeloItemAvaliacaoService {

    private final ModeloItemAvaliacaoRepository modeloItemAvaliacaoRepository;

    public ModeloItemAvaliacao getById(@NonNull Long id) {
        return modeloItemAvaliacaoRepository.getReferenceById(id);
    }

    public ListResponse<ModeloItemAvaliacaoProjection> search(@NonNull Long idModeloAvaliacao, ModeloItemAvaliacaoGridOptions options) throws BusinessException {
        var page = modeloItemAvaliacaoRepository.search(idModeloAvaliacao, options.getTipoTcc(), options.getTipoProfessor(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public ModeloItemAvaliacao insert(@NonNull ModeloItemAvaliacao modeloItemAvaliacao) throws BusinessException {
        modeloItemAvaliacao.setId(null);
        validate(modeloItemAvaliacao);
        return modeloItemAvaliacaoRepository.save(modeloItemAvaliacao);
    }

    public ModeloItemAvaliacao update(@NonNull Long id, @NonNull ModeloItemAvaliacao modeloItemAvaliacao) throws BusinessException {
        modeloItemAvaliacao.setId(id);
        validate(modeloItemAvaliacao);
        return modeloItemAvaliacaoRepository.save(modeloItemAvaliacao);
    }

    public void deleteLogic(@NonNull Long id) throws BusinessException {
        var modeloItemAvaliacao = modeloItemAvaliacaoRepository.getReferenceById(id);
        if (modeloItemAvaliacao.getDataExclusao() != null) {
            throw new BusinessException("Item já deletado");
        }
        modeloItemAvaliacao.setDataExclusao(LocalDateTime.now());
        modeloItemAvaliacaoRepository.save(modeloItemAvaliacao);
    }

    private void validate(ModeloItemAvaliacao modeloItemAvaliacao) throws BusinessException {
        var errors = new ArrayList<String>();

        if (modeloItemAvaliacao.getTipoTccs() == null || modeloItemAvaliacao.getTipoTccs().isEmpty()) {
            errors.add("Tipo de TCC não informado");
        }

        if (modeloItemAvaliacao.getTipoProfessores() == null || modeloItemAvaliacao.getTipoProfessores().isEmpty()) {
            errors.add("Tipo de Professor não informado");
        }

        if (modeloItemAvaliacao.getModeloAspectosAvaliacao() == null || modeloItemAvaliacao.getModeloAspectosAvaliacao().isEmpty()) {
            errors.add("Deve ser informado pelo menos um aspecto de avaliação");
        }

        if (errors.isEmpty()) {
            var modeloItemAvaliacaoResumeList = getValidate(modeloItemAvaliacao.getTipoTccs(), modeloItemAvaliacao.getTipoProfessores(), modeloItemAvaliacao.getId());
            if (!modeloItemAvaliacaoResumeList.isEmpty()) {
                var errorsDuplicate = modeloItemAvaliacaoResumeList.stream().map(m -> {
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

    private List<ModeloAvaliacaoResumeDTO> getValidate(Set<TipoTcc> tipoTccs, Set<TipoProfessor> tipoProfessores, Long id) {
        var modeloItens = modeloItemAvaliacaoRepository.getValidateByTipoTccsAndTipoProfessoresAndNotId(tipoTccs, tipoProfessores, id);
        return modeloItens.isEmpty() ?
                Collections.emptyList() :
                modeloItens.stream()
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

    public List<ModeloItemAvaliacao> getItensByAreaTccAndTipoTcc(Long idAreaTcc, TipoTcc tipoTcc) {
        return modeloItemAvaliacaoRepository.getItensByAreaTccAndTipoTcc(idAreaTcc, tipoTcc);
    }

    public ModeloItemAvaliacao getByAreaTccAndTipoTccAndTipoProfessor(Long idAreaTcc, TipoTcc tipoTcc, TipoProfessor tipoProfessor) {
        var itens = modeloItemAvaliacaoRepository.getByAreaTccAndTipoTccAndTipoProfessor(idAreaTcc, tipoTcc, tipoProfessor);
        return itens.isEmpty() ? null : itens.get(0);
    }

}
