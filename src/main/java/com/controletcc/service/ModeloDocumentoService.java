package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloDocumentoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloDocumento;
import com.controletcc.repository.ModeloDocumentoRepository;
import com.controletcc.repository.projection.ModeloDocumentoProjection;
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
public class ModeloDocumentoService {

    private final ModeloDocumentoRepository modeloDocumentoRepository;

    public List<ModeloDocumento> getAll() {
        return modeloDocumentoRepository.findAll();
    }

    public ModeloDocumento getById(@NonNull Long id) {
        return modeloDocumentoRepository.getReferenceById(id);
    }

    public ListResponse<ModeloDocumentoProjection> search(ModeloDocumentoGridOptions options) {
        var page = modeloDocumentoRepository.search(options.getId(), options.getNome(), options.getDescricao(), options.getTipoTcc(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public ModeloDocumento insert(@NonNull ModeloDocumento modeloDocumento) throws BusinessException {
        modeloDocumento.setId(null);
        validate(modeloDocumento);
        return modeloDocumentoRepository.save(modeloDocumento);
    }

    public ModeloDocumento update(@NonNull Long id, @NonNull ModeloDocumento modeloDocumento) throws BusinessException {
        modeloDocumento.setId(id);
        validate(modeloDocumento);
        return modeloDocumentoRepository.save(modeloDocumento);
    }

    private void validate(ModeloDocumento modeloDocumento) throws BusinessException {
        var errors = new ArrayList<String>();

        if (StringUtil.isNullOrBlank(modeloDocumento.getNome())) {
            errors.add("Nome do modelo não informado");
        } else {
            if (modeloDocumento.getId() == null) {
                if (modeloDocumentoRepository.existsByNome(modeloDocumento.getNome())) {
                    errors.add("Já existe outro modelo com este nome");
                }
            } else if (modeloDocumentoRepository.existsByNomeAndIdNot(modeloDocumento.getNome(), modeloDocumento.getId())) {
                errors.add("Já existe outro modelo com este nome");
            }
        }

        if (StringUtil.isNullOrBlank(modeloDocumento.getDescricao())) {
            errors.add("Descrição do modelo não informado");
        }

        if (modeloDocumento.getTipoTccs() == null || modeloDocumento.getTipoTccs().isEmpty()) {
            errors.add("Tipo de TCC não informado");
        }

        if (!modeloDocumento.isFileValid()) {
            errors.add("Arquivo não informado");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

}
