package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloItemAvaliacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ModeloItemAvaliacaoDTO;
import com.controletcc.model.entity.ModeloAspectoAvaliacao;
import com.controletcc.model.entity.ModeloItemAvaliacao;
import com.controletcc.repository.projection.ModeloItemAvaliacaoProjection;
import com.controletcc.service.ModeloAspectoAvaliacaoService;
import com.controletcc.service.ModeloItemAvaliacaoService;
import com.controletcc.service.ProfessorService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ModeloItemAvaliacaoFacade {

    private final ModeloItemAvaliacaoService modeloItemAvaliacaoService;

    private final ModeloAspectoAvaliacaoService modeloAspectoAvaliacaoService;

    private final ProfessorService professorService;

    public ModeloItemAvaliacaoDTO getById(Long id) {
        var modeloItemAvaliacao = modeloItemAvaliacaoService.getById(id);
        return ModelMapperUtil.map(modeloItemAvaliacao, ModeloItemAvaliacaoDTO.class);
    }

    public ListResponse<ModeloItemAvaliacaoProjection> search(Long idModeloAvaliacao, ModeloItemAvaliacaoGridOptions options) throws BusinessException {
        return modeloItemAvaliacaoService.search(idModeloAvaliacao, options);
    }

    public ModeloItemAvaliacaoDTO insert(ModeloItemAvaliacaoDTO modeloItemAvaliacaoDTO) throws Exception {
        var modeloItemAvaliacao = ModelMapperUtil.map(modeloItemAvaliacaoDTO, ModeloItemAvaliacao.class);
        var modeloAspectosAvaliacao = modeloItemAvaliacao.getModeloAspectosAvaliacao();
        modeloItemAvaliacao = modeloItemAvaliacaoService.insert(modeloItemAvaliacao);
        modeloItemAvaliacao.setModeloAspectosAvaliacao(saveItens(modeloItemAvaliacao, modeloAspectosAvaliacao));
        return ModelMapperUtil.map(modeloItemAvaliacao, ModeloItemAvaliacaoDTO.class);
    }

    public ModeloItemAvaliacaoDTO update(ModeloItemAvaliacaoDTO modeloItemAvaliacaoDTO) throws Exception {
        var modeloItemAvaliacao = ModelMapperUtil.map(modeloItemAvaliacaoDTO, ModeloItemAvaliacao.class);
        var modeloAspectosAvaliacao = modeloItemAvaliacao.getModeloAspectosAvaliacao();
        modeloItemAvaliacao = modeloItemAvaliacaoService.update(modeloItemAvaliacao.getId(), modeloItemAvaliacao);
        modeloItemAvaliacao.setModeloAspectosAvaliacao(saveItens(modeloItemAvaliacao, modeloAspectosAvaliacao));
        return ModelMapperUtil.map(modeloItemAvaliacao, ModeloItemAvaliacaoDTO.class);
    }

    public void deleteLogic(Long id) throws Exception {
        modeloItemAvaliacaoService.deleteLogic(id);
    }

    private List<ModeloAspectoAvaliacao> saveItens(ModeloItemAvaliacao modeloItemAvaliacao, List<ModeloAspectoAvaliacao> modeloAspectosAvaliacao) throws BusinessException {
        for (var aspecto : modeloAspectosAvaliacao) {
            aspecto.setModeloItemAvaliacao(modeloItemAvaliacao);
        }
        return modeloAspectoAvaliacaoService.saveAll(modeloItemAvaliacao.getId(), modeloAspectosAvaliacao);
    }

}

