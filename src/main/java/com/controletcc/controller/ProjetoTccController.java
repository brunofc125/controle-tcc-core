package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProjetoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ProjetoTccFacade;
import com.controletcc.model.dto.ProjetoTccDTO;
import com.controletcc.repository.projection.ProjetoTccProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/projetos-tcc")
@RequiredArgsConstructor
public class ProjetoTccController {

    private final ProjetoTccFacade projetoTccFacade;

    @PreAuthorize("hasAuthority('projeto-tcc.read')")
    @GetMapping("{id}")
    public ProjetoTccDTO getById(@PathVariable Long id) {
        return projetoTccFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('projeto-tcc.read')")
    @PostMapping("search")
    public ListResponse<ProjetoTccProjection> search(@RequestBody ProjetoTccGridOptions options) throws BusinessException {
        return projetoTccFacade.search(options);
    }

    @PreAuthorize("hasAuthority('projeto-tcc.create')")
    @PostMapping
    public ProjetoTccDTO insert(@RequestBody ProjetoTccDTO projetoTcc) throws BusinessException {
        return projetoTccFacade.insert(projetoTcc);
    }

    @PreAuthorize("hasAuthority('projeto-tcc.create')")
    @PutMapping("{id}")
    public ProjetoTccDTO update(@PathVariable Long id, @RequestBody ProjetoTccDTO projetoTcc) throws BusinessException {
        projetoTcc.setId(id);
        return projetoTccFacade.update(projetoTcc);
    }

}
