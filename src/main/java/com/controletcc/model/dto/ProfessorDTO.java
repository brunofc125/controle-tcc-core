package com.controletcc.model.dto;

import com.controletcc.dto.csv.ProfessorImportCsvDTO;
import com.controletcc.model.dto.base.PessoaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorDTO extends PessoaDTO {
    private Long id;
    private boolean supervisorTcc;
    private Long idUsuario;
    private List<Long> idAreaList;

    public ProfessorDTO(ProfessorImportCsvDTO csv) {
        this.nome = csv.getNome();
        this.email = csv.getEmail();
        this.sexo = csv.getSexo();
        this.dataNascimento = csv.getDataNascimento();
        this.supervisorTcc = csv.isSupervisorTcc();
        this.idAreaList = csv.getIdAreasTcc();
    }
}
