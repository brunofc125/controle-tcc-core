package com.controletcc.dto.csv;

import com.controletcc.annotation.CsvColumn;
import com.controletcc.dto.csv.type.LongType;
import com.controletcc.dto.enums.CsvType;
import com.controletcc.model.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorImportCsvDTO extends BaseImportCsvDTO {
    @CsvColumn(name = "CPF", type = CsvType.STRING)
    private String cpf;

    @CsvColumn(name = "Nome", type = CsvType.STRING)
    private String nome;

    @CsvColumn(name = "RG", type = CsvType.STRING)
    private String rg;

    @CsvColumn(name = "E-mail", type = CsvType.STRING)
    private String email;

    @CsvColumn(name = "Sexo", type = CsvType.ENUM, enumClass = Sexo.class)
    private Sexo sexo;

    @CsvColumn(name = "Data de nascimento", type = CsvType.LOCAL_DATE)
    private LocalDate dataNascimento;

    @CsvColumn(name = "Supervisor de TCC", type = CsvType.BOOLEAN)
    private boolean supervisorTcc;

    @CsvColumn(name = "ID's Áreas de TCC", type = CsvType.LIST, listClass = LongType.class)
    private List<Long> idAreasTcc;

}
