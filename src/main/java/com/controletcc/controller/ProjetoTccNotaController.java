package com.controletcc.controller;

import com.controletcc.facade.ProjetoTccNotaFacade;
import com.controletcc.model.dto.ProjetoTccNotaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/projeto-tcc-notas")
@RequiredArgsConstructor
public class ProjetoTccNotaController {

    private final ProjetoTccNotaFacade projetoTccNotaFacade;

    @PreAuthorize("hasAuthority('projeto-tcc-nota.read')")
    @GetMapping("projeto-tcc/{idProjetoTcc}")
    public ProjetoTccNotaDTO getByProjetoTcc(@PathVariable Long idProjetoTcc) throws Exception {
        return projetoTccNotaFacade.getByProjetoTcc(idProjetoTcc);
    }

    @PreAuthorize("hasAuthority('projeto-tcc-nota.grade')")
    @PatchMapping("lancar-nota/{idProjetoTcc}")
    public ProjetoTccNotaDTO lancarNota(@PathVariable Long idProjetoTcc) throws Exception {
        return projetoTccNotaFacade.lancarNota(idProjetoTcc);
    }

}
