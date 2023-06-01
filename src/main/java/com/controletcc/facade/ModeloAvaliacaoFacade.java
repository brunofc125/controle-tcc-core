package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloAvaliacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ModeloAvaliacaoDTO;
import com.controletcc.model.entity.ModeloAvaliacao;
import com.controletcc.repository.projection.ModeloAvaliacaoProjection;
import com.controletcc.service.ModeloAvaliacaoService;
import com.controletcc.service.ProfessorService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ModeloAvaliacaoFacade {

    private final ModeloAvaliacaoService modeloAvaliacaoService;

    private final ProfessorService professorService;

    public ModeloAvaliacaoDTO getById(Long id) {
        var modeloAvaliacao = modeloAvaliacaoService.getById(id);
        return ModelMapperUtil.map(modeloAvaliacao, ModeloAvaliacaoDTO.class);
    }

    public ListResponse<ModeloAvaliacaoProjection> search(ModeloAvaliacaoGridOptions options) throws BusinessException {
        var professor = professorService.getProfessorLogado();
        return modeloAvaliacaoService.search(professor.getIdAreaList(), options);
    }

    public ModeloAvaliacaoDTO insert(ModeloAvaliacaoDTO modeloAvaliacaoDTO) throws Exception {
        var modeloAvaliacao = ModelMapperUtil.map(modeloAvaliacaoDTO, ModeloAvaliacao.class);
        modeloAvaliacao = modeloAvaliacaoService.insert(modeloAvaliacao);
        return ModelMapperUtil.map(modeloAvaliacao, ModeloAvaliacaoDTO.class);
    }

    public ModeloAvaliacaoDTO update(ModeloAvaliacaoDTO modeloAvaliacaoDTO) throws Exception {
        var modeloAvaliacao = ModelMapperUtil.map(modeloAvaliacaoDTO, ModeloAvaliacao.class);
        modeloAvaliacao = modeloAvaliacaoService.update(modeloAvaliacao.getId(), modeloAvaliacao);
        return ModelMapperUtil.map(modeloAvaliacao, ModeloAvaliacaoDTO.class);
    }

    public void deleteLogic(Long id) throws Exception {
        modeloAvaliacaoService.deleteLogic(id);
    }

}

