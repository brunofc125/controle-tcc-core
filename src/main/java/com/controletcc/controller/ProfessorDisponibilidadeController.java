package com.controletcc.controller;

import com.controletcc.error.BusinessException;
import com.controletcc.facade.ProfessorDisponibilidadeFacade;
import com.controletcc.model.dto.ProfessorDisponibilidadeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAuthority('professor-disponibilidade.create')")
    @PostMapping
    public List<ProfessorDisponibilidadeDTO> save(@RequestBody List<ProfessorDisponibilidadeDTO> professorDisponibilidades) throws BusinessException {
        return professorDisponibilidadeFacade.save(professorDisponibilidades);
    }

    @PreAuthorize("hasAuthority('professor-disponibilidade.delete')")
    @DeleteMapping("{id}/{outrasOcorrencias}")
    public void delete(@PathVariable Long id, @PathVariable boolean outrasOcorrencias) throws Exception {
        professorDisponibilidadeFacade.delete(id, outrasOcorrencias);
    }

}
