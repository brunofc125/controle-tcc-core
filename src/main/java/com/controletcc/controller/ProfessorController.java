package com.controletcc.controller;

import com.controletcc.dto.SaveProfessorDTO;
import com.controletcc.dto.base.ListResponseModel;
import com.controletcc.dto.options.ProfessorGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ProfessorFacade;
import com.controletcc.model.dto.ProfessorDTO;
import com.controletcc.repository.projection.ProfessorProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorFacade professorFacade;

    @PreAuthorize("hasAuthority('professor.read')")
    @GetMapping("{id}")
    public ProfessorDTO getById(@PathVariable Long id) {
        return professorFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('professor.read')")
    @PostMapping("search")
    public ListResponseModel<ProfessorProjection> search(@RequestBody ProfessorGridOptions options) throws BusinessException {
        return professorFacade.search(options);
    }

    @PreAuthorize("hasAuthority('professor.create')")
    @PostMapping
    public ProfessorDTO insert(@RequestBody SaveProfessorDTO saveProfessor) throws BusinessException {
        return professorFacade.insert(saveProfessor);
    }

    @PreAuthorize("hasAuthority('professor.create')")
    @PutMapping("{id}")
    public ProfessorDTO update(@PathVariable Long id, @RequestBody ProfessorDTO professor) throws BusinessException {
        professor.setId(id);
        return professorFacade.update(professor);
    }

}
