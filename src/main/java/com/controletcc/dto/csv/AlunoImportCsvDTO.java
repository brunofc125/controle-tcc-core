package com.controletcc.dto.csv;

import com.controletcc.annotation.CsvColumn;
import com.controletcc.dto.enums.CsvType;
import com.controletcc.model.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlunoImportCsvDTO extends BaseImportCsvDTO {

    @CsvColumn(name = "Nome", type = CsvType.STRING)
    private String nome;

    @CsvColumn(name = "E-mail", type = CsvType.STRING)
    private String email;

    @CsvColumn(name = "Sexo (MASC/FEM/INDF)", type = CsvType.ENUM, enumClass = Sexo.class)
    private Sexo sexo;

    @CsvColumn(name = "Data de nascimento (dd/MM/yyyy)", type = CsvType.LOCAL_DATE)
    private LocalDate dataNascimento;

    @CsvColumn(name = "Matricula", type = CsvType.STRING)
    private String matricula;

    @CsvColumn(name = "ID Área de TCC (Consulte o Sistema)", type = CsvType.LONG)
    private Long idAreaTcc;

}
