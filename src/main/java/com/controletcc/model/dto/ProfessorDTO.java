package com.controletcc.model.dto;

import com.controletcc.model.dto.base.PessoaDTO;
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
}
