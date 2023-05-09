package com.controletcc.dto.csv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReturnExportCsvDTO {
    private String fileName;
    private int qtdRecords;
    private String base64;
}
