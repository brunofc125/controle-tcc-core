package com.controletcc.controller;

import com.controletcc.dto.base.ListResponseModel;
import com.controletcc.dto.grid.ProfessorGridDTO;
import com.controletcc.dto.options.ProfessorGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("search")
    public ListResponseModel<ProfessorGridDTO> search(@RequestBody ProfessorGridOptions options) throws BusinessException {
        return professorService.search(options);
    }

}
