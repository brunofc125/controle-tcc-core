package com.controletcc.controller;

import com.controletcc.dto.SaveProfessorDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.csv.ReturnImportCsvDTO;
import com.controletcc.dto.options.ProfessorGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.ProfessorFacade;
import com.controletcc.facade.ProfessorImportFacade;
import com.controletcc.model.dto.ProfessorDTO;
import com.controletcc.repository.projection.ProfessorProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorFacade professorFacade;
    private final ProfessorImportFacade professorImportFacade;

    @PreAuthorize("hasAuthority('professor.read')")
    @GetMapping("{id}")
    public ProfessorDTO getById(@PathVariable Long id) {
        return professorFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('professor.read')")
    @PostMapping("search")
    public ListResponse<ProfessorProjection> search(@RequestBody ProfessorGridOptions options) throws BusinessException {
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

    @PreAuthorize("hasAuthority('professor.import')")
    @GetMapping("modelo-importacao")
    public void getModeloImportacao(HttpServletResponse response) throws BusinessException, IOException {
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=professor_modelo_importacao.csv";
        response.setHeader(headerKey, headerValue);
        professorImportFacade.getModeloImportacao(response.getWriter());
    }

    @PreAuthorize("hasAuthority('professor.import')")
    @PostMapping("import")
    public ReturnImportCsvDTO importFile(@RequestParam("file") MultipartFile file) throws Exception {
        return professorImportFacade.importFile(file);
    }

}
