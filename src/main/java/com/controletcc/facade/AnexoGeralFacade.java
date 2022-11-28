package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AnexoGeralGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.AnexoGeralDTO;
import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.model.entity.AnexoGeral;
import com.controletcc.model.entity.base.Arquivo;
import com.controletcc.repository.projection.AnexoGeralProjection;
import com.controletcc.service.AnexoGeralService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AnexoGeralFacade {

    private final AnexoGeralService anexoGeralService;

    public AnexoGeralDTO getById(Long id) {
        var anexoGeral = anexoGeralService.getById(id);
        return ModelMapperUtil.map(anexoGeral, AnexoGeralDTO.class);
    }

    public ListResponse<AnexoGeralProjection> search(Long idProjetoTcc, AnexoGeralGridOptions options) {
        return anexoGeralService.search(idProjetoTcc, options);
    }

    public AnexoGeralDTO insert(AnexoGeralDTO anexoGeralDTO) throws Exception {
        var anexoGeral = ModelMapperUtil.map(anexoGeralDTO, AnexoGeral.class);
        anexoGeral = anexoGeralService.insert(anexoGeral);
        return ModelMapperUtil.map(anexoGeral, AnexoGeralDTO.class);
    }

    public void delete(Long id) throws Exception {
        anexoGeralService.delete(id);
    }

    public ArquivoDTO download(Long id) {
        Arquivo anexoGeral = anexoGeralService.getById(id);
        return ModelMapperUtil.map(anexoGeral, ArquivoDTO.class);
    }

}

