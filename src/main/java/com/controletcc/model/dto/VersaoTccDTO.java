package com.controletcc.model.dto;

import com.controletcc.model.dto.base.ArquivoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VersaoTccDTO extends ArquivoDTO {
    private Long id;
    private String observacao;
    private Long versao;
    private Long idProjetoTcc;
    private Long idProfessorOrientador;
    private Long idAluno;
}
