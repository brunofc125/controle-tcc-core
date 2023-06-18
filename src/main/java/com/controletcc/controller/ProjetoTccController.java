package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.csv.ReturnExportCsvDTO;
import com.controletcc.dto.enums.TccRoute;
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
    @PostMapping("search/{tccRoute}")
    public ListResponse<ProjetoTccProjection> search(@PathVariable TccRoute tccRoute, @RequestBody ProjetoTccGridOptions options) throws BusinessException {
        return projetoTccFacade.search(tccRoute, options);
    }

    @PreAuthorize("hasAuthority('projeto-tcc.export')")
    @PostMapping("export/{tccRoute}")
    public ReturnExportCsvDTO export(@PathVariable TccRoute tccRoute, @RequestBody ProjetoTccGridOptions options) throws Exception {
        return projetoTccFacade.export(tccRoute, options);
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

    @PreAuthorize("hasAuthority('projeto-tcc.cancel')")
    @PatchMapping("cancelar/{id}")
    public void cancelar(@PathVariable Long id, @RequestBody String motivo) throws BusinessException {
        projetoTccFacade.cancelar(id, motivo);
    }

    @PreAuthorize("hasAuthority('projeto-tcc.reprove')")
    @PatchMapping("reprovar/{id}")
    public void reprovar(@PathVariable Long id, @RequestBody String motivo) throws BusinessException {
        projetoTccFacade.reprovar(id, motivo);
    }

    @PreAuthorize("hasAuthority('projeto-tcc.read')")
    @GetMapping("valido-agendar-apresentacao/{id}")
    public void validoAgendarApresentacao(@PathVariable Long id) throws BusinessException {
        projetoTccFacade.validoAgendarApresentacao(id);
    }

    @PreAuthorize("hasAuthority('projeto-tcc.create')")
    @PatchMapping("to-defesa/{id}")
    public void avancarParaDefesa(@PathVariable Long id) throws BusinessException {
        projetoTccFacade.avancarParaDefesa(id);
    }

    @PreAuthorize("hasAuthority('projeto-tcc.read')")
    @PatchMapping("visualizar-doc/{id}")
    public void visualizarDoc(@PathVariable Long id) {
        projetoTccFacade.visualizarDoc(id);
    }

}
