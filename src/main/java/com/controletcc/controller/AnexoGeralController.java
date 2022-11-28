package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AnexoGeralGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.AnexoGeralFacade;
import com.controletcc.model.dto.AnexoGeralDTO;
import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.repository.projection.AnexoGeralProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/anexos-gerais")
@RequiredArgsConstructor
public class AnexoGeralController {

    private final AnexoGeralFacade anexoGeralFacade;

    @PreAuthorize("hasAuthority('anexo-geral.read')")
    @GetMapping("{id}")
    public AnexoGeralDTO getById(@PathVariable Long id) {
        return anexoGeralFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('anexo-geral.read')")
    @PostMapping("search/{idProjetoTcc}")
    public ListResponse<AnexoGeralProjection> search(@PathVariable Long idProjetoTcc, @RequestBody AnexoGeralGridOptions options) throws BusinessException {
        return anexoGeralFacade.search(idProjetoTcc, options);
    }

    @PreAuthorize("hasAuthority('anexo-geral.create')")
    @PostMapping
    public AnexoGeralDTO insert(@RequestBody AnexoGeralDTO modeloDocumento) throws Exception {
        return anexoGeralFacade.insert(modeloDocumento);
    }

    @PreAuthorize("hasAuthority('anexo-geral.delete')")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws Exception {
        anexoGeralFacade.delete(id);
    }

    @PreAuthorize("hasAuthority('anexo-geral.read')")
    @GetMapping("download/{id}")
    public ArquivoDTO download(@PathVariable Long id) {
        return anexoGeralFacade.download(id);
    }

}
