package com.controletcc.controller;

import com.controletcc.dto.ProfessorCompromissosDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProfessorCompromissoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ProfessorCompromissoFacade;
import com.controletcc.model.dto.ProfessorCompromissoDTO;
import com.controletcc.repository.projection.ProfessorCompromissoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/professor-compromissos")
@RequiredArgsConstructor
public class ProfessorCompromissoController {

    private final ProfessorCompromissoFacade professorCompromissoFacade;

    @PreAuthorize("hasAuthority('professor-compromisso.read')")
    @GetMapping("{id}")
    public ProfessorCompromissoDTO getById(@PathVariable Long id) {
        return professorCompromissoFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('professor-compromisso.read')")
    @PostMapping("search")
    public ListResponse<ProfessorCompromissoProjection> search(@RequestBody ProfessorCompromissoGridOptions options) throws BusinessException {
        return professorCompromissoFacade.search(options);
    }

    @PreAuthorize("hasAuthority('professor-compromisso.create')")
    @PostMapping
    public List<ProfessorCompromissoDTO> save(@RequestBody List<ProfessorCompromissoDTO> professorCompromissos) throws BusinessException {
        return professorCompromissoFacade.save(professorCompromissos);
    }

    @PreAuthorize("hasAuthority('professor-compromisso.delete')")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws Exception {
        professorCompromissoFacade.delete(id);
    }

    @PreAuthorize("hasAuthority('professor-compromisso.read')")
    @PostMapping("compromissos-by-professor-logado")
    public ProfessorCompromissosDTO getCompromissosByProfessorLogadoAndData(@RequestBody LocalDate dataInicial) throws BusinessException {
        return professorCompromissoFacade.getCompromissosByProfessorLogadoAndData(dataInicial);
    }

}
