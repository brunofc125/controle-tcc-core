package com.controletcc.dto.csv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReturnImportCsvDTO {
    private int qtdRecords;
    private int qtdRecordsError;
    private byte[] file;
}
