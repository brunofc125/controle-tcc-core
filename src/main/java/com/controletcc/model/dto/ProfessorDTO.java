package com.controletcc.model.dto;

import com.controletcc.dto.csv.ProfessorImportCsvDTO;
import com.controletcc.model.dto.base.PessoaDTO;
import com.controletcc.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorDTO extends PessoaDTO {
    private Long id;
    private boolean supervisorTcc;
    private Long idUsuario;

    public ProfessorDTO(ProfessorImportCsvDTO csv) {
        this.cpf = StringUtil.getOnlyNumbers(csv.getCpf());
        this.nome = csv.getNome();
        this.rg = csv.getRg();
        this.email = csv.getEmail();
        this.sexo = csv.getSexo();
        this.dataNascimento = csv.getDataNascimento();
        this.supervisorTcc = csv.isSupervisorTcc();
    }
}
