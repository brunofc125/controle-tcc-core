package com.controletcc.dto.options;

import com.controletcc.dto.options.base.BaseGridOptions;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccGridOptions extends BaseGridOptions {

    private Long id;
    private String tema;
    private String anoPeriodo;
    private TipoTcc tipoTcc;
    private SituacaoTcc situacaoTcc;
    private String nomeProfessorOrientador;
    private String nomeAluno;

}
