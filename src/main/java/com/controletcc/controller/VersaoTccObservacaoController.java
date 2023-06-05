package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.base.BaseGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.VersaoTccObservacaoFacade;
import com.controletcc.model.dto.VersaoTccObservacaoDTO;
import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.repository.projection.VersaoTccObservacaoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/versoes-tcc-observacoes")
@RequiredArgsConstructor
public class VersaoTccObservacaoController {

    private final VersaoTccObservacaoFacade versaoTccObservacaoFacade;

    @PreAuthorize("hasAuthority('versao-tcc-observacao.read')")
    @GetMapping("{id}")
    public VersaoTccObservacaoDTO getById(@PathVariable Long id) {
        return versaoTccObservacaoFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('versao-tcc-observacao.read')")
    @PostMapping("search/{idVersaoTcc}/{onlyAvaliacao}")
    public ListResponse<VersaoTccObservacaoProjection> search(@PathVariable Long idVersaoTcc, @PathVariable boolean onlyAvaliacao, @RequestBody BaseGridOptions options) throws BusinessException {
        return versaoTccObservacaoFacade.search(idVersaoTcc, onlyAvaliacao, options);
    }

    @PreAuthorize("hasAuthority('versao-tcc-observacao.create')")
    @PostMapping
    public VersaoTccObservacaoDTO insert(@RequestBody VersaoTccObservacaoDTO versaoTccObservacaoDTO) throws Exception {
        return versaoTccObservacaoFacade.insert(versaoTccObservacaoDTO);
    }

    @PreAuthorize("hasAuthority('versao-tcc-observacao.read')")
    @GetMapping("download/{id}")
    public ArquivoDTO download(@PathVariable Long id) {
        return versaoTccObservacaoFacade.download(id);
    }

}
