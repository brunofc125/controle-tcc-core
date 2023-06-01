package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloItemAvaliacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ModeloItemAvaliacaoFacade;
import com.controletcc.model.dto.ModeloItemAvaliacaoDTO;
import com.controletcc.repository.projection.ModeloItemAvaliacaoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/modelo-itens-avaliacoes")
@RequiredArgsConstructor
public class ModeloItemAvaliacaoController {

    private final ModeloItemAvaliacaoFacade modeloItemAvaliacaoFacade;

    @PreAuthorize("hasAuthority('modelo-avaliacao.read')")
    @GetMapping("{id}")
    public ModeloItemAvaliacaoDTO getById(@PathVariable Long id) {
        return modeloItemAvaliacaoFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('modelo-avaliacao.read')")
    @PostMapping("search/{idModeloAvaliacao}")
    public ListResponse<ModeloItemAvaliacaoProjection> search(@PathVariable Long idModeloAvaliacao, @RequestBody ModeloItemAvaliacaoGridOptions options) throws BusinessException {
        return modeloItemAvaliacaoFacade.search(idModeloAvaliacao, options);
    }

    @PreAuthorize("hasAuthority('modelo-avaliacao.create')")
    @PostMapping
    public ModeloItemAvaliacaoDTO insert(@RequestBody ModeloItemAvaliacaoDTO modeloItemAvaliacao) throws Exception {
        return modeloItemAvaliacaoFacade.insert(modeloItemAvaliacao);
    }

    @PreAuthorize("hasAuthority('modelo-avaliacao.create')")
    @PutMapping("{id}")
    public ModeloItemAvaliacaoDTO update(@PathVariable Long id, @RequestBody ModeloItemAvaliacaoDTO modeloItemAvaliacao) throws Exception {
        modeloItemAvaliacao.setId(id);
        return modeloItemAvaliacaoFacade.update(modeloItemAvaliacao);
    }

    @PreAuthorize("hasAuthority('modelo-avaliacao.delete')")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws Exception {
        modeloItemAvaliacaoFacade.deleteLogic(id);
    }

}
