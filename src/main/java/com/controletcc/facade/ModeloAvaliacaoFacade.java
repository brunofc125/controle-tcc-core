package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloAvaliacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ModeloAvaliacaoDTO;
import com.controletcc.model.entity.ModeloAvaliacao;
import com.controletcc.model.entity.ModeloItemAvaliacao;
import com.controletcc.repository.projection.ModeloAvaliacaoProjection;
import com.controletcc.service.ModeloAvaliacaoService;
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
public class ModeloAvaliacaoFacade {

    private final ModeloAvaliacaoService modeloAvaliacaoService;

    private final ModeloItemAvaliacaoService modeloItemAvaliacaoService;

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
        var modeloItensAvaliacao = modeloAvaliacao.getModeloItensAvaliacao();
        modeloAvaliacao = modeloAvaliacaoService.insert(modeloAvaliacao);
        modeloAvaliacao.setModeloItensAvaliacao(saveItens(modeloAvaliacao, modeloItensAvaliacao));
        return ModelMapperUtil.map(modeloAvaliacao, ModeloAvaliacaoDTO.class);
    }

    public ModeloAvaliacaoDTO update(ModeloAvaliacaoDTO modeloAvaliacaoDTO) throws Exception {
        var modeloAvaliacao = ModelMapperUtil.map(modeloAvaliacaoDTO, ModeloAvaliacao.class);
        var modeloItensAvaliacao = modeloAvaliacao.getModeloItensAvaliacao();
        modeloAvaliacao = modeloAvaliacaoService.update(modeloAvaliacao.getId(), modeloAvaliacao);
        modeloAvaliacao.setModeloItensAvaliacao(saveItens(modeloAvaliacao, modeloItensAvaliacao));
        return ModelMapperUtil.map(modeloAvaliacao, ModeloAvaliacaoDTO.class);
    }

    private List<ModeloItemAvaliacao> saveItens(ModeloAvaliacao modeloAvaliacao, List<ModeloItemAvaliacao> modeloItensAvaliacao) throws BusinessException {
        for (var item : modeloItensAvaliacao) {
            item.setModeloAvaliacao(modeloAvaliacao);
        }
        return modeloItemAvaliacaoService.saveAll(modeloAvaliacao.getId(), modeloItensAvaliacao);
    }

}

