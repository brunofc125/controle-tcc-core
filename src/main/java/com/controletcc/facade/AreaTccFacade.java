package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AreaTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.AreaTccDTO;
import com.controletcc.model.entity.AreaTcc;
import com.controletcc.repository.projection.AreaTccProjection;
import com.controletcc.service.AreaTccService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AreaTccFacade {

    private final AreaTccService areaTccService;

    public AreaTccDTO getById(Long id) {
        var areaTcc = areaTccService.getById(id);
        return ModelMapperUtil.map(areaTcc, AreaTccDTO.class);
    }

    public ListResponse<AreaTccProjection> search(AreaTccGridOptions options) {
        return areaTccService.search(options);
    }

    public AreaTccDTO insert(AreaTccDTO areaTccDTO) throws BusinessException {
        var areaTcc = ModelMapperUtil.map(areaTccDTO, AreaTcc.class);
        areaTcc = areaTccService.insert(areaTcc);
        return ModelMapperUtil.map(areaTcc, AreaTccDTO.class);
    }

    public AreaTccDTO update(AreaTccDTO areaTccDTO) throws BusinessException {
        var areaTcc = ModelMapperUtil.map(areaTccDTO, AreaTcc.class);
        areaTcc = areaTccService.update(areaTcc.getId(), areaTcc);
        return ModelMapperUtil.map(areaTcc, AreaTccDTO.class);
    }

}

