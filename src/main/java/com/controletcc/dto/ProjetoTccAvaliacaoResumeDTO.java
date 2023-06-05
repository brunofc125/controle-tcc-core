package com.controletcc.dto;

import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoTccAvaliacaoResumeDTO {
    private Long id;
    private TipoTcc tipoTcc;
    private TipoProfessor tipoProfessor;
    private Long idProjetoTcc;
    private Long idProfessor;
    private String nomeProfessor;
    private Double nota;
}
