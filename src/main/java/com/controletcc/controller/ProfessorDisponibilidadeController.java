package com.controletcc.controller;

import com.controletcc.dto.ProfessorCompromissosDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProfessorDisponibilidadeGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ProfessorDisponibilidadeFacade;
import com.controletcc.model.dto.ProfessorDisponibilidadeDTO;
import com.controletcc.repository.projection.ProfessorDisponibilidadeProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/professor-disponibilidades")
@RequiredArgsConstructor
public class ProfessorDisponibilidadeController {

    private final ProfessorDisponibilidadeFacade professorDisponibilidadeFacade;

    @PreAuthorize("hasAuthority('professor-disponibilidade.read')")
    @GetMapping("{id}")
    public ProfessorDisponibilidadeDTO getById(@PathVariable Long id) {
        return professorDisponibilidadeFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('professor-disponibilidade.read')")
    @PostMapping("search")
    public ListResponse<ProfessorDisponibilidadeProjection> search(@RequestBody ProfessorDisponibilidadeGridOptions options) throws BusinessException {
        return professorDisponibilidadeFacade.search(options);
    }

    @PreAuthorize("hasAuthority('professor-disponibilidade.create')")
    @PostMapping
    public List<ProfessorDisponibilidadeDTO> save(@RequestBody List<ProfessorDisponibilidadeDTO> professorCompromissos) throws BusinessException {
        return professorDisponibilidadeFacade.save(professorCompromissos);
    }

    @PreAuthorize("hasAuthority('professor-disponibilidade.delete')")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws Exception {
        professorDisponibilidadeFacade.delete(id);
    }

    @PreAuthorize("hasAuthority('professor-disponibilidade.read')")
    @PostMapping("compromissos-by-professor-logado")
    public ProfessorCompromissosDTO getCompromissosByProfessorLogadoAndData(@RequestBody LocalDate dataInicial) throws BusinessException {
        return professorDisponibilidadeFacade.getCompromissosByProfessorLogadoAndData(dataInicial);
    }

}
