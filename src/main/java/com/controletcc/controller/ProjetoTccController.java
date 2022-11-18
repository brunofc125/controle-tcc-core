package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProjetoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ProjetoTccFacade;
import com.controletcc.repository.projection.ProjetoTccProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projetos-tcc")
@RequiredArgsConstructor
public class ProjetoTccController {

    private final ProjetoTccFacade projetoTccFacade;

    @PreAuthorize("hasAuthority('projeto-tcc.read')")
    @PostMapping("search")
    public ListResponse<ProjetoTccProjection> search(@RequestBody ProjetoTccGridOptions options) throws BusinessException {
        return projetoTccFacade.search(options);
    }

}
