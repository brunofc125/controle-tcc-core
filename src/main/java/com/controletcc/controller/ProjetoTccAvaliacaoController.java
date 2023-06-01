package com.controletcc.controller;

import com.controletcc.facade.ProjetoTccAvaliacaoFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projeto-tcc-avaliacoes")
@RequiredArgsConstructor
public class ProjetoTccAvaliacaoController {

    private final ProjetoTccAvaliacaoFacade projetoTccAvaliacaoFacade;

    @PreAuthorize("hasAuthority('projeto-tcc-avaliacao.create')")
    @PatchMapping("iniciar-etapa-avaliacao/{idProjetoTcc}")
    public void iniciarEtapaAvaliacao(@PathVariable Long idProjetoTcc) throws Exception {
        projetoTccAvaliacaoFacade.iniciarEtapaAvaliacao(idProjetoTcc);
    }

}
