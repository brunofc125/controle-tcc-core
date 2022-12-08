package com.controletcc.service;

import com.controletcc.annotation.CsvColumn;
import com.controletcc.dto.csv.BaseImportCsvDTO;
import com.controletcc.dto.csv.ReturnImportCsvDTO;
import com.controletcc.error.BusinessException;
import com.controletcc.error.CsvErrorException;
import com.controletcc.util.ErrorUtil;
import com.controletcc.util.FileAppUtil;
import com.controletcc.util.LocalDateUtil;
import com.controletcc.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvService {

    public static final String COLUMN_NAME_ERROR = "Erros";
    public static final String COLUMN_LIST_DELIMITER = "-";

    public <T extends BaseImportCsvDTO> ReturnImportCsvDTO getModelCsv(String fileName, Class<T> clazz) throws Exception {
        var returnImportCsv = new ReturnImportCsvDTO();
        returnImportCsv.setQtdRecords(0);
        returnImportCsv.setQtdRecordsError(0);

        var fileCsv = File.createTempFile(fileName, ".csv");
        try (var writer = new FileWriter(fileCsv)) {
            getCSVPrinter(writer, clazz);
            returnImportCsv.setBase64(FileAppUtil.fileToBase64(fileCsv));
        } catch (Exception e) {
            log.error("Erro na geração do arquivo de modelo.", e);
        }
        fileCsv.deleteOnExit();

        return returnImportCsv;
    }

    private <T extends BaseImportCsvDTO> void getCSVPrinter(Writer writer, Class<T> clazz) throws BusinessException {
        try (var printer = new CSVPrinter(writer, CSVFormat.Builder.create().setHeader(this.getHeader(clazz, false)).build())) {
            printer.flush();
        } catch (Exception ex) {
            ErrorUtil.error(log, ex, "Erro na geração do arquivo de modelo.");
        }
    }

    public <T extends BaseImportCsvDTO> ReturnImportCsvDTO getImportedCsv(String fileName, List<T> records, Class<T> clazz) throws Exception {
        var recordsWithError = records.stream().filter(r -> !r.isValid()).toList();
        var returnImportCsv = new ReturnImportCsvDTO();
        returnImportCsv.setQtdRecords(records.size());
        returnImportCsv.setQtdRecordsError(recordsWithError.size());
        if (recordsWithError.size() > 0) {
            var fileCsv = File.createTempFile(fileName, ".csv");
            try (var writer = new FileWriter(fileCsv)) {
                getCSVPrinterError(writer, clazz, recordsWithError);
                returnImportCsv.setBase64(FileAppUtil.fileToBase64(fileCsv));
            } catch (Exception e) {
                log.error("Erro ao gerar o arquivo de erros de importação.", e);
            }
            fileCsv.deleteOnExit();
        }
        return returnImportCsv;
    }

    private <T extends BaseImportCsvDTO> void getCSVPrinterError(Writer writer, Class<T> clazz, List<T> recordsError) throws BusinessException {
        try (var printer = new CSVPrinter(writer, CSVFormat.Builder.create().setHeader(this.getHeader(clazz, true)).build())) {
            printer.printRecords(getValueRecords(clazz, recordsError, true));
            printer.flush();
        } catch (Exception ex) {
            ErrorUtil.error(log, ex, "Erro ao gerar o arquivo de erros de importação.");
        }
    }

    private <T extends BaseImportCsvDTO> CSVParser getCSVParser(MultipartFile file, Class<T> clazz) throws BusinessException {
        try {
            return CSVParser.parse(file.getInputStream(), StandardCharsets.US_ASCII,
                    CSVFormat.Builder.create()
                            .setHeader(this.getHeader(clazz, false))
                            .setSkipHeaderRecord(true)
                            .setIgnoreEmptyLines(true)
                            .build());
        } catch (Exception ex) {
            ErrorUtil.error(log, ex, "Erro ao importar o arquivo.");
        }
        return null;
    }

    public <T extends BaseImportCsvDTO> List<T> getRecords(MultipartFile file, Class<T> clazz) throws BusinessException {
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().endsWith(".csv")) {
            throw new BusinessException("Arquivo inválido, apenas CSV é suportado");
        }
        var list = new ArrayList<T>();
        try {
            var csvParser = this.getCSVParser(file, clazz);
            if (csvParser != null) {
                for (var csvRecord : csvParser) {
                    var record = clazz.getDeclaredConstructor().newInstance();
                    buildRecord(csvRecord, record);
                    list.add(record);
                }
            }
        } catch (Exception ex) {
            ErrorUtil.error(log, ex, "Erro ao gerar os registros do arquivo.");
        }
        return list;
    }

    private <T extends BaseImportCsvDTO> void buildRecord(CSVRecord csvRecord, T record) {
        for (var field : record.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(CsvColumn.class)) {
                var csvColumn = field.getAnnotation(CsvColumn.class);
                var value = csvRecord.get(csvColumn.name());
                try {
                    switch (csvColumn.type()) {
                        case STRING -> setStringField(field, record, csvColumn, value);
                        case LONG -> setLongField(field, record, csvColumn, value);
                        case BOOLEAN -> setBooleanField(field, record, csvColumn, value);
                        case LOCAL_DATE -> setLocalDateField(field, record, csvColumn, value);
                        case ENUM -> setEnumField(field, record, csvColumn, value);
                        case LIST -> setListField(field, record, csvColumn, value);
                    }
                } catch (CsvErrorException e) {
                    record.addError(e.getMessage());
                }
            }
        }
    }

    private <T extends BaseImportCsvDTO> void setStringField(Field field, T record, CsvColumn csvColumn, String value) throws CsvErrorException {
        try {
            field.set(record, value);
        } catch (Exception e) {
            var msgError = "Erro na coluna " + csvColumn.name() + ": Valor inválido";
            log.error(msgError, e);
            throw new CsvErrorException(msgError);
        }
    }

    private <T extends BaseImportCsvDTO> void setLongField(Field field, T record, CsvColumn csvColumn, String value) throws CsvErrorException {
        try {
            field.set(record, StringUtil.isNullOrBlank(value) ? null : Long.parseLong(value));
        } catch (Exception e) {
            var msgError = "Erro na coluna " + csvColumn.name() + ": Valor inválido";
            log.error(msgError, e);
            throw new CsvErrorException(msgError);
        }
    }

    private <T extends BaseImportCsvDTO> void setBooleanField(Field field, T record, CsvColumn csvColumn, String value) throws CsvErrorException {
        try {
            var booleanValue = value.equalsIgnoreCase("s") || value.equalsIgnoreCase("sim");
            field.set(record, booleanValue);
        } catch (Exception e) {
            var msgError = "Erro na coluna " + csvColumn.name() + ": Valor inválido, deveria ser S/N";
            log.error(msgError, e);
            throw new CsvErrorException(msgError);
        }
    }

    private <T extends BaseImportCsvDTO> void setLocalDateField(Field field, T record, CsvColumn csvColumn, String value) throws CsvErrorException {
        try {
            field.set(record, LocalDateUtil.stringToLocalDate(value, "dd/MM/yyyy"));
        } catch (Exception e) {
            var msgError = "Erro na coluna " + csvColumn.name() + ": Valor inválido, deveria seguir o formato dd/MM/yyyy";
            log.error(msgError, e);
            throw new CsvErrorException(msgError);
        }
    }

    private <T extends BaseImportCsvDTO> void setEnumField(Field field, T record, CsvColumn csvColumn, String value) throws CsvErrorException {
        var enums = csvColumn.enumClass().getEnumConstants();
        try {
            var enumValue = !StringUtil.isNullOrBlank(value) ? Arrays.stream(enums).filter(e -> e.name().equalsIgnoreCase(value)).findAny().orElseThrow() : null;
            field.set(record, enumValue);
        } catch (Exception e) {
            var enumsStr = Arrays.stream(enums).map(Enum::name).collect(Collectors.joining("/"));
            var msgError = "Erro na coluna " + csvColumn.name() + ": Valor inválido, deveria ser " + enumsStr;
            log.error(msgError, e);
            throw new CsvErrorException(msgError);
        }
    }

    private <T extends BaseImportCsvDTO> void setListField(Field field, T record, CsvColumn csvColumn, String value) throws CsvErrorException {
        if (StringUtil.isNullOrBlank(value)) {
            return;
        }
        String typeName = null;
        try {
            var typeCast = csvColumn.listClass().getDeclaredConstructor().newInstance();
            typeName = typeCast.typeName();
            var values = Arrays.stream(value.split(COLUMN_LIST_DELIMITER)).map(typeCast::cast).toList();
            field.set(record, values);
        } catch (Exception e) {
            var msgError = "Erro na coluna " + csvColumn.name() + ": Valor inválido, deveria ser uma lista separada por \"" + COLUMN_LIST_DELIMITER + "\"" + (typeName != null ? " do tipo " + typeName : "");
            log.error(msgError, e);
            throw new CsvErrorException(msgError);
        }
    }

    private <T extends BaseImportCsvDTO> String[] getHeader(Class<T> clazz, boolean addError) {
        var fields = new ArrayList<String>();
        for (var field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(CsvColumn.class)) {
                var csvColumn = field.getAnnotation(CsvColumn.class);
                fields.add(csvColumn.name());
            }
        }
        if (addError) {
            fields.add(COLUMN_NAME_ERROR);
        }
        var arrFields = new String[fields.size()];
        return fields.toArray(arrFields);
    }

    private <T extends BaseImportCsvDTO> List<String[]> getValueRecords(Class<T> clazz, List<T> records, boolean withError) throws Exception {
        var valueRecords = new ArrayList<String[]>();
        for (var record : records) {
            var line = new ArrayList<String>();
            for (var field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(CsvColumn.class)) {
                    var csvColumn = field.getAnnotation(CsvColumn.class);
                    switch (csvColumn.type()) {
                        case STRING -> line.add(getStringField(field, record));
                        case LONG -> line.add(getLongField(field, record));
                        case BOOLEAN -> line.add(getBooleanField(field, record));
                        case LOCAL_DATE -> line.add(getLocalDateField(field, record));
                        case ENUM -> line.add(getEnumField(field, record, csvColumn));
                        case LIST -> line.add(getListField(field, record, csvColumn));
                    }
                }
            }
            if (withError) {
                line.add(record.getError());
            }
            var arrLine = new String[line.size()];
            valueRecords.add(line.toArray(arrLine));
        }
        return valueRecords;
    }

    private <T extends BaseImportCsvDTO> String getStringField(Field field, T record) throws Exception {
        if (field.get(record) == null) {
            return null;
        }
        return (String) field.get(record);
    }

    private <T extends BaseImportCsvDTO> String getLongField(Field field, T record) throws Exception {
        if (field.get(record) == null) {
            return null;
        }
        return field.get(record).toString();
    }

    private <T extends BaseImportCsvDTO> String getBooleanField(Field field, T record) throws Exception {
        if (field.get(record) == null) {
            return null;
        }
        return ((boolean) field.get(record)) ? "S" : "N";
    }

    private <T extends BaseImportCsvDTO> String getLocalDateField(Field field, T record) throws Exception {
        if (field.get(record) == null) {
            return null;
        }
        var localDate = (LocalDate) field.get(record);
        return LocalDateUtil.localDateToString(localDate, "dd/MM/yyyy");
    }

    private <T extends BaseImportCsvDTO, E extends Enum<E>> String getEnumField(Field field, T record, CsvColumn csvColumn) throws Exception {
        if (field.get(record) == null) {
            return null;
        }
        return csvColumn.enumClass().cast(field.get(record)).name();
    }

    private <T extends BaseImportCsvDTO> String getListField(Field field, T record, CsvColumn csvColumn) throws Exception {
        if (field.get(record) == null) {
            return null;
        }
        var typeCast = csvColumn.listClass().getDeclaredConstructor().newInstance();
        var valuesObj = (List<?>) field.get(record);
        List<String> values = valuesObj != null ? valuesObj.stream().map(typeCast::toString).toList() : Collections.emptyList();
        return !values.isEmpty() ? String.join(COLUMN_LIST_DELIMITER, values) : "";
    }

}
