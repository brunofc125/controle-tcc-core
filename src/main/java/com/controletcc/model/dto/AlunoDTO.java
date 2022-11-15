package com.controletcc.model.dto;

import com.controletcc.dto.csv.AlunoImportCsvDTO;
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
public class AlunoDTO extends PessoaDTO {
    private Long id;
    private Long idUsuario;
    private String matricula;
    private Long idAreaTcc;

    public AlunoDTO(AlunoImportCsvDTO csv) {
        this.cpf = StringUtil.getOnlyNumbers(csv.getCpf());
        this.nome = csv.getNome();
        this.rg = csv.getRg();
        this.email = csv.getEmail();
        this.sexo = csv.getSexo();
        this.dataNascimento = csv.getDataNascimento();
        this.matricula = csv.getMatricula();
        this.idAreaTcc = csv.getIdAreaTcc();
    }
}
