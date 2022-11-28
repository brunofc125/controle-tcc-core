package com.controletcc.controller;

import com.controletcc.dto.SaveAlunoDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.csv.ReturnImportCsvDTO;
import com.controletcc.dto.options.AlunoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.AlunoFacade;
import com.controletcc.facade.AlunoImportFacade;
import com.controletcc.model.dto.AlunoDTO;
import com.controletcc.repository.projection.AlunoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoFacade alunoFacade;
    private final AlunoImportFacade alunoImportFacade;

    @PreAuthorize("hasAnyAuthority('aluno.read', 'aluno.perfil')")
    @GetMapping("{id}")
    public AlunoDTO getById(@PathVariable Long id) {
        return alunoFacade.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('aluno.read', 'projeto-tcc.read')")
    @GetMapping("area-tcc/{idAreaTcc}")
    public List<AlunoDTO> getAllByIdAreaTcc(@PathVariable Long idAreaTcc) {
        return alunoFacade.getAllByIdAreaTcc(idAreaTcc);
    }

    @PreAuthorize("hasAuthority('aluno.read')")
    @PostMapping("search")
    public ListResponse<AlunoProjection> search(@RequestBody AlunoGridOptions options) throws BusinessException {
        return alunoFacade.search(options);
    }

    @PreAuthorize("hasAuthority('aluno.create')")
    @PostMapping
    public AlunoDTO insert(@RequestBody SaveAlunoDTO saveAluno) throws BusinessException {
        return alunoFacade.insert(saveAluno);
    }

    @PreAuthorize("hasAnyAuthority('aluno.create', 'aluno.perfil')")
    @PutMapping("{id}")
    public AlunoDTO update(@PathVariable Long id, @RequestBody AlunoDTO aluno) throws BusinessException {
        aluno.setId(id);
        return alunoFacade.update(aluno);
    }

    @PreAuthorize("hasAuthority('aluno.import')")
    @GetMapping("modelo-importacao")
    public ReturnImportCsvDTO getModelCsv() throws Exception {
        return alunoImportFacade.getModelCsv();
    }

    @PreAuthorize("hasAuthority('aluno.import')")
    @PostMapping("import")
    public ReturnImportCsvDTO importFile(@RequestParam("file") MultipartFile file) throws Exception {
        return alunoImportFacade.importFile(file);
    }

}
