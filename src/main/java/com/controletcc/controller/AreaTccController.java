package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AreaTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.AreaTccFacade;
import com.controletcc.model.dto.AreaTccDTO;
import com.controletcc.repository.projection.AreaTccProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/areas-tcc")
@RequiredArgsConstructor
public class AreaTccController {

    private final AreaTccFacade areaTccFacade;

    @PreAuthorize("hasAuthority('area-tcc.read')")
    @GetMapping("{id}")
    public AreaTccDTO getById(@PathVariable Long id) {
        return areaTccFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('area-tcc.read')")
    @GetMapping
    public List<AreaTccDTO> getAll() {
        return areaTccFacade.getAll();
    }

    @PreAuthorize("hasAuthority('area-tcc.read')")
    @PostMapping("search")
    public ListResponse<AreaTccProjection> search(@RequestBody AreaTccGridOptions options) throws BusinessException {
        return areaTccFacade.search(options);
    }

    @PreAuthorize("hasAuthority('area-tcc.create')")
    @PostMapping
    public AreaTccDTO insert(@RequestBody AreaTccDTO areaTcc) throws BusinessException {
        return areaTccFacade.insert(areaTcc);
    }

    @PreAuthorize("hasAuthority('area-tcc.create')")
    @PutMapping("{id}")
    public AreaTccDTO update(@PathVariable Long id, @RequestBody AreaTccDTO areaTcc) throws BusinessException {
        areaTcc.setId(id);
        return areaTccFacade.update(areaTcc);
    }

}
