package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloAvaliacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ModeloAvaliacaoFacade;
import com.controletcc.model.dto.ModeloAvaliacaoDTO;
import com.controletcc.repository.projection.ModeloAvaliacaoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/modelo-avaliacoes")
@RequiredArgsConstructor
public class ModeloAvaliacaoController {

    private final ModeloAvaliacaoFacade modeloAvaliacaoFacade;

    @PreAuthorize("hasAuthority('modelo-avaliacao.read')")
    @GetMapping("{id}")
    public ModeloAvaliacaoDTO getById(@PathVariable Long id) {
        return modeloAvaliacaoFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('modelo-avaliacao.read')")
    @PostMapping("search")
    public ListResponse<ModeloAvaliacaoProjection> search(@RequestBody ModeloAvaliacaoGridOptions options) throws BusinessException {
        return modeloAvaliacaoFacade.search(options);
    }

    @PreAuthorize("hasAuthority('modelo-avaliacao.create')")
    @PostMapping
    public ModeloAvaliacaoDTO insert(@RequestBody ModeloAvaliacaoDTO modeloAvaliacao) throws Exception {
        return modeloAvaliacaoFacade.insert(modeloAvaliacao);
    }

    @PreAuthorize("hasAuthority('modelo-avaliacao.create')")
    @PutMapping("{id}")
    public ModeloAvaliacaoDTO update(@PathVariable Long id, @RequestBody ModeloAvaliacaoDTO modeloAvaliacao) throws Exception {
        modeloAvaliacao.setId(id);
        return modeloAvaliacaoFacade.update(modeloAvaliacao);
    }

    @PreAuthorize("hasAuthority('modelo-avaliacao.delete')")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws Exception {
        modeloAvaliacaoFacade.deleteLogic(id);
    }

}
