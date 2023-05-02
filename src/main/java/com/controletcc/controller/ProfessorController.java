package com.controletcc.controller;

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

import java.util.List;

@RestController
@RequestMapping("api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorFacade professorFacade;
    private final ProfessorImportFacade professorImportFacade;

    @PreAuthorize("hasAnyAuthority('professor.read', 'professor.perfil', 'projeto-tcc.read')")
    @GetMapping("{id}")
    public ProfessorDTO getById(@PathVariable Long id) {
        return professorFacade.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('professor.read', 'projeto-tcc.read')")
    @GetMapping("supervisor-by-area-tcc/{idAreaTcc}")
    public List<ProfessorDTO> getSupervisoresByIdAreaTcc(@PathVariable Long idAreaTcc) {
        return professorFacade.getSupervisoresByIdAreaTcc(idAreaTcc);
    }

    @PreAuthorize("hasAuthority('professor.read')")
    @PostMapping("search")
    public ListResponse<ProfessorProjection> search(@RequestBody ProfessorGridOptions options) throws BusinessException {
        return professorFacade.search(options);
    }

    @PreAuthorize("hasAuthority('professor.create')")
    @PostMapping
    public ProfessorDTO insert(@RequestBody ProfessorDTO professor) throws BusinessException {
        return professorFacade.insert(professor);
    }

    @PreAuthorize("hasAnyAuthority('professor.create', 'professor.perfil')")
    @PutMapping("{id}")
    public ProfessorDTO update(@PathVariable Long id, @RequestBody ProfessorDTO professor) throws BusinessException {
        professor.setId(id);
        return professorFacade.update(professor);
    }

    @PreAuthorize("hasAuthority('professor.import')")
    @GetMapping("modelo-importacao")
    public ReturnImportCsvDTO getModelCsv() throws Exception {
        return professorImportFacade.getModelCsv();
    }

    @PreAuthorize("hasAuthority('professor.import')")
    @PostMapping("import")
    public ReturnImportCsvDTO importFile(@RequestParam("file") MultipartFile file) throws Exception {
        return professorImportFacade.importFile(file);
    }

    @PreAuthorize("hasAnyAuthority('professor.read', 'membro-banca.read')")
    @GetMapping("candidato-banca/{idProjetoTcc}")
    public List<ProfessorDTO> getCandidatosBanca(@PathVariable Long idProjetoTcc) throws BusinessException {
        return professorFacade.getCandidatosBanca(idProjetoTcc);
    }

}
