package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloDocumentoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ModeloDocumentoDTO;
import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.model.entity.ModeloDocumento;
import com.controletcc.model.entity.base.Arquivo;
import com.controletcc.repository.projection.ModeloDocumentoProjection;
import com.controletcc.service.ModeloDocumentoService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ModeloDocumentoFacade {

    private final ModeloDocumentoService modeloDocumentoService;

    public ModeloDocumentoDTO getById(Long id) {
        var modeloDocumento = modeloDocumentoService.getById(id);
        return ModelMapperUtil.map(modeloDocumento, ModeloDocumentoDTO.class);
    }

    public ListResponse<ModeloDocumentoProjection> search(ModeloDocumentoGridOptions options) {
        return modeloDocumentoService.search(options);
    }

    public ModeloDocumentoDTO insert(ModeloDocumentoDTO modeloDocumentoDTO) throws Exception {
        var modeloDocumento = ModelMapperUtil.map(modeloDocumentoDTO, ModeloDocumento.class);
        modeloDocumento = modeloDocumentoService.insert(modeloDocumento);
        return ModelMapperUtil.map(modeloDocumento, ModeloDocumentoDTO.class);
    }

    public ModeloDocumentoDTO update(ModeloDocumentoDTO modeloDocumentoDTO) throws Exception {
        var modeloDocumento = ModelMapperUtil.map(modeloDocumentoDTO, ModeloDocumento.class);
        modeloDocumento = modeloDocumentoService.update(modeloDocumento.getId(), modeloDocumento);
        return ModelMapperUtil.map(modeloDocumento, ModeloDocumentoDTO.class);
    }

    public ArquivoDTO download(Long id) {
        Arquivo modeloDocumento = modeloDocumentoService.getById(id);
        return ModelMapperUtil.map(modeloDocumento, ArquivoDTO.class);
    }

}

