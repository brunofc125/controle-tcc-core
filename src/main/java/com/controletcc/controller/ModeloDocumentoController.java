package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ModeloDocumentoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ModeloDocumentoFacade;
import com.controletcc.model.dto.ModeloDocumentoDTO;
import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.repository.projection.ModeloDocumentoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/modelo-documentos")
@RequiredArgsConstructor
public class ModeloDocumentoController {

    private final ModeloDocumentoFacade modeloDocumentoFacade;

    @PreAuthorize("hasAuthority('modelo-documento.read')")
    @GetMapping("{id}")
    public ModeloDocumentoDTO getById(@PathVariable Long id) {
        return modeloDocumentoFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('modelo-documento.read')")
    @PostMapping("search")
    public ListResponse<ModeloDocumentoProjection> search(@RequestBody ModeloDocumentoGridOptions options) throws BusinessException {
        return modeloDocumentoFacade.search(options);
    }

    @PreAuthorize("hasAuthority('modelo-documento.create')")
    @PostMapping
    public ModeloDocumentoDTO insert(@RequestBody ModeloDocumentoDTO modeloDocumento) throws Exception {
        return modeloDocumentoFacade.insert(modeloDocumento);
    }

    @PreAuthorize("hasAuthority('modelo-documento.create')")
    @PutMapping("{id}")
    public ModeloDocumentoDTO update(@PathVariable Long id, @RequestBody ModeloDocumentoDTO modeloDocumento) throws Exception {
        modeloDocumento.setId(id);
        return modeloDocumentoFacade.update(modeloDocumento);
    }

    @PreAuthorize("hasAuthority('modelo-documento.read')")
    @GetMapping("download/{id}")
    public ArquivoDTO download(@PathVariable Long id) {
        return modeloDocumentoFacade.download(id);
    }

}
