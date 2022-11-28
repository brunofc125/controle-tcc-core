package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.VersaoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.VersaoTccFacade;
import com.controletcc.model.dto.VersaoTccDTO;
import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.repository.projection.VersaoTccProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/versoes-tcc")
@RequiredArgsConstructor
public class VersaoTccController {

    private final VersaoTccFacade versaoTccFacade;

    @PreAuthorize("hasAuthority('versao-tcc.read')")
    @GetMapping("{id}")
    public VersaoTccDTO getById(@PathVariable Long id) {
        return versaoTccFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('versao-tcc.read')")
    @PostMapping("search/{idProjetoTcc}")
    public ListResponse<VersaoTccProjection> search(@PathVariable Long idProjetoTcc, @RequestBody VersaoTccGridOptions options) throws BusinessException {
        return versaoTccFacade.search(idProjetoTcc, options);
    }

    @PreAuthorize("hasAuthority('versao-tcc.create')")
    @PostMapping
    public VersaoTccDTO insert(@RequestBody VersaoTccDTO versaoTccDTO) throws Exception {
        return versaoTccFacade.insert(versaoTccDTO);
    }

    @PreAuthorize("hasAuthority('versao-tcc.read')")
    @GetMapping("download/{id}")
    public ArquivoDTO download(@PathVariable Long id) {
        return versaoTccFacade.download(id);
    }

}
