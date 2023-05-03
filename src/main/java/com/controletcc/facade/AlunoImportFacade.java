package com.controletcc.facade;

import com.controletcc.dto.csv.AlunoImportCsvDTO;
import com.controletcc.dto.csv.ReturnImportCsvDTO;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.AlunoDTO;
import com.controletcc.service.CsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AlunoImportFacade {

    private final AlunoFacade alunoFacade;

    private final CsvService csvService;

    public ReturnImportCsvDTO getModelCsv() throws Exception {
        return this.csvService.getModelCsv("aluno_import", AlunoImportCsvDTO.class);
    }

    public ReturnImportCsvDTO importFile(MultipartFile file) throws Exception {
        var records = this.csvService.getRecords(file, AlunoImportCsvDTO.class);
        if (records.isEmpty()) {
            throw new BusinessException("Não há registros nesta planilha");
        }
        for (var record : records) {
            var alunoDTO = new AlunoDTO(record);
            try {
                alunoFacade.insertTransactional(alunoDTO);
            } catch (BusinessException be) {
                record.addError(String.join("\n", be.getErrors()));
            } catch (Exception ex) {
                var uuid = UUID.randomUUID();
                log.error("ERRO: " + uuid + " - " + ex.getMessage() + " - " + ex.getCause(), ex);
                record.setError("Erro na importação deste registro. Código do erro: " + uuid);
            }
        }
        return csvService.getImportedCsv("aluno_import_error", records, AlunoImportCsvDTO.class);
    }

}

