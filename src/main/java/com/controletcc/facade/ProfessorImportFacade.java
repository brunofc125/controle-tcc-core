package com.controletcc.facade;

import com.controletcc.dto.csv.ProfessorImportCsvDTO;
import com.controletcc.dto.csv.ReturnImportCsvDTO;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProfessorDTO;
import com.controletcc.model.dto.UserDTO;
import com.controletcc.service.CsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProfessorImportFacade {

    private final ProfessorFacade professorFacade;

    private final CsvService csvService;

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
                    professorFacade.insertTransactional(professorDTO, userDTO);
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
                var bytes = FileUtil.readAsByteArray(fileCsv);
                var base64 = Base64.getEncoder().encode(bytes);
                returnImportCsv.setBase64(new String(base64, StandardCharsets.UTF_8));
            } catch (Exception e) {
                log.error("Erro na geração do arquivo de erro", e);
            }
            fileCsv.deleteOnExit();
        }
        return returnImportCsv;
    }

}

