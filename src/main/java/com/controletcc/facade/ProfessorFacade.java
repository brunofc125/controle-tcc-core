package com.controletcc.facade;

import com.controletcc.dto.SaveProfessorDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.csv.ProfessorImportCsvDTO;
import com.controletcc.dto.csv.ReturnImportCsvDTO;
import com.controletcc.dto.options.ProfessorGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProfessorDTO;
import com.controletcc.model.dto.UserDTO;
import com.controletcc.model.entity.Professor;
import com.controletcc.model.entity.User;
import com.controletcc.model.enums.UserType;
import com.controletcc.repository.projection.ProfessorProjection;
import com.controletcc.service.CsvService;
import com.controletcc.service.ProfessorService;
import com.controletcc.service.UserService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProfessorFacade {

    private final ProfessorService professorService;

    private final UserService userService;

    private final CsvService csvService;

    public ProfessorDTO getById(Long id) {
        var professor = professorService.getById(id);
        return ModelMapperUtil.map(professor, ProfessorDTO.class);
    }

    public ListResponse<ProfessorProjection> search(ProfessorGridOptions options) {
        return professorService.search(options);
    }

    public ProfessorDTO insert(SaveProfessorDTO saveProfessor) throws BusinessException {
        var professor = ModelMapperUtil.map(saveProfessor.getProfessor(), Professor.class);
        var usuario = ModelMapperUtil.map(saveProfessor.getUser(), User.class);
        usuario.setName(professor.getNome());
        usuario = userService.insert(usuario, professor.isSupervisorTcc() ? UserType.SUPERVISOR : UserType.PROFESSOR);
        professor.setUsuario(usuario);
        professor = professorService.insert(professor);
        return ModelMapperUtil.map(professor, ProfessorDTO.class);
    }

    public ProfessorDTO update(ProfessorDTO professorDTO) throws BusinessException {
        var professor = ModelMapperUtil.map(professorDTO, Professor.class);
        var professorBanco = professorService.getById(professor.getId());
        if (professor.isSupervisorTcc() != professorBanco.isSupervisorTcc()) {
            var userType = professor.isSupervisorTcc() ? UserType.SUPERVISOR : UserType.PROFESSOR;
            userService.updateRoles(professorBanco.getIdUsuario(), userType);
        }
        userService.updateName(professorBanco.getIdUsuario(), professor.getNome());
        professor = professorService.update(professor.getId(), professor);
        return ModelMapperUtil.map(professor, ProfessorDTO.class);
    }

    public void getModeloImportacao(Writer writer) throws BusinessException {
        this.csvService.getCSVPrinter(writer, ProfessorImportCsvDTO.class);
    }

    public ReturnImportCsvDTO importFile(MultipartFile file) throws Exception {
        var records = this.csvService.getRecords(file, ProfessorImportCsvDTO.class);
        for (var record : records) {
            if (record.isValid()) {
                var userDTO = new UserDTO(record);
                var professorDTO = new ProfessorDTO(record);
                try {
                    this.insertTransactional(professorDTO, userDTO);
                } catch (BusinessException be) {
                    record.setError(String.join("\n", be.getErrors()));
                }
            }
        }
        var recordsWithError = records.stream().filter(r -> !r.isValid()).toList();
        var returnImportCsv = new ReturnImportCsvDTO();
        returnImportCsv.setQtdRecords(records.size());
        returnImportCsv.setQtdRecordsError(recordsWithError.size());
        if (recordsWithError.size() > 0) {
            var fileCsv = File.createTempFile("professor_import_error", ".csv");
            try (var writer = new FileWriter(fileCsv)) {
                csvService.getCSVPrinterError(writer, ProfessorImportCsvDTO.class, recordsWithError);
                returnImportCsv.setFile(Files.readAllBytes(Path.of(fileCsv.getAbsolutePath())));
            } catch (Exception e) {
                log.error("Erro na geração do arquivo de erro", e);
            }
            fileCsv.deleteOnExit();
        }
        return returnImportCsv;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertTransactional(ProfessorDTO professorDTO, UserDTO userDTO) throws BusinessException {
        var professor = ModelMapperUtil.map(professorDTO, Professor.class);
        var usuario = ModelMapperUtil.map(userDTO, User.class);
        usuario.setName(professor.getNome());
        usuario = userService.insert(usuario, professor.isSupervisorTcc() ? UserType.SUPERVISOR : UserType.PROFESSOR);
        professor.setUsuario(usuario);
        professorService.insert(professor);
    }

}

