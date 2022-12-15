package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.MembroBancaFacade;
import com.controletcc.model.dto.MembroBancaDTO;
import com.controletcc.repository.projection.MembroBancaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/membros-banca")
@RequiredArgsConstructor
public class MembroBancaController {

    private final MembroBancaFacade membroBancaFacade;

    @PreAuthorize("hasAuthority('membro-banca.read')")
    @GetMapping("{id}")
    public MembroBancaDTO getById(@PathVariable Long id) {
        return membroBancaFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('membro-banca.read')")
    @GetMapping("projeto-tcc/{idProjetoTcc}")
    public ListResponse<MembroBancaProjection> getAllByIdProjetoTcc(@PathVariable Long idProjetoTcc) throws BusinessException {
        return membroBancaFacade.getAllByIdProjetoTcc(idProjetoTcc);
    }

    @PreAuthorize("hasAuthority('membro-banca.create')")
    @PostMapping("solicitar/{idProjetoTcc}/{idProfessor}")
    public MembroBancaDTO solicitar(@PathVariable Long idProjetoTcc, @PathVariable Long idProfessor) throws BusinessException {
        return membroBancaFacade.solicitar(idProjetoTcc, idProfessor);
    }

    @PreAuthorize("hasAuthority('membro-banca.create')")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws BusinessException {
        membroBancaFacade.delete(id);
    }

    @PreAuthorize("hasAuthority('membro-banca.create')")
    @PatchMapping("confirmar/{idProjetoTcc}")
    public void confirmar(@PathVariable Long idProjetoTcc) throws BusinessException {
        membroBancaFacade.confirmar(idProjetoTcc);
    }

    @PreAuthorize("hasAuthority('membro-banca.create')")
    @PatchMapping("desconfirmar/{idProjetoTcc}")
    public void desconfirmar(@PathVariable Long idProjetoTcc) throws BusinessException {
        membroBancaFacade.desconfirmar(idProjetoTcc);
    }

}
