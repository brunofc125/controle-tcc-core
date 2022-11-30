package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.base.BaseGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.VersaoTccObservacaoDTO;
import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.model.entity.VersaoTccObservacao;
import com.controletcc.model.entity.base.ArquivoOpcional;
import com.controletcc.repository.projection.VersaoTccObservacaoProjection;
import com.controletcc.service.VersaoTccObservacaoService;
import com.controletcc.service.VersaoTccService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class VersaoTccObservacaoFacade {

    private final VersaoTccObservacaoService versaoTccObservacaoService;

    private final VersaoTccService versaoTccService;

    public VersaoTccObservacaoDTO getById(Long id) {
        var versaoTccObservacao = versaoTccObservacaoService.getById(id);
        return ModelMapperUtil.map(versaoTccObservacao, VersaoTccObservacaoDTO.class);
    }

    public ListResponse<VersaoTccObservacaoProjection> search(Long idVersaoTcc, BaseGridOptions options) {
        return versaoTccObservacaoService.search(idVersaoTcc, options);
    }

    public VersaoTccObservacaoDTO insert(VersaoTccObservacaoDTO versaoTccObservacaoDTO) throws Exception {
        var versaoTccObservacao = ModelMapperUtil.map(versaoTccObservacaoDTO, VersaoTccObservacao.class);
        var versaoTcc = versaoTccService.getById(versaoTccObservacaoDTO.getIdVersaoTcc());
        versaoTccObservacao = versaoTccObservacaoService.insert(versaoTccObservacao, versaoTcc);
        return ModelMapperUtil.map(versaoTccObservacao, VersaoTccObservacaoDTO.class);
    }

    public ArquivoDTO download(Long id) {
        ArquivoOpcional anexoGeral = versaoTccObservacaoService.getById(id);
        return ModelMapperUtil.map(anexoGeral, ArquivoDTO.class);
    }

}

