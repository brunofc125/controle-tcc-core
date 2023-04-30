package com.controletcc.controller;

import com.controletcc.dto.AgendaPeriodoDTO;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ApresentacaoFacade;
import com.controletcc.model.dto.ApresentacaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/apresentacoes")
@RequiredArgsConstructor
public class ApresentacaoController {

    private final ApresentacaoFacade apresentacaoFacade;

    @PreAuthorize("hasAuthority('apresentacao.read')")
    @GetMapping("projeto-tcc/{idProjetoTcc}")
    public ApresentacaoDTO getByProjetoTcc(@PathVariable Long idProjetoTcc) {
        return apresentacaoFacade.getByProjetoTcc(idProjetoTcc);
    }

    @PreAuthorize("hasAuthority('apresentacao.create')")
    @PostMapping
    public ApresentacaoDTO insert(@RequestBody ApresentacaoDTO apresentacao) throws BusinessException {
        return apresentacaoFacade.insert(apresentacao);
    }

    @PreAuthorize("hasAuthority('apresentacao.create')")
    @PutMapping("{id}")
    public ApresentacaoDTO update(@PathVariable Long id, @RequestBody ApresentacaoDTO apresentacao) throws BusinessException {
        apresentacao.setId(id);
        return apresentacaoFacade.update(apresentacao);
    }

    @PreAuthorize("hasAuthority('apresentacao.read')")
    @GetMapping("professor/anoPeriodo/{anoPeriodo}")
    public AgendaPeriodoDTO getAgendaByProfessorLogadoAndAnoPeriodo(@PathVariable String anoPeriodo) throws BusinessException {
        return apresentacaoFacade.getAllByProfessorLogadoAndAnoPeriodo(anoPeriodo);
    }

}
