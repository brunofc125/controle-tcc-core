package com.controletcc.dto.options;

import com.controletcc.dto.options.base.BaseGridOptions;
import com.controletcc.model.enums.TipoCompromisso;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorDisponibilidadeGridOptions extends BaseGridOptions {

    private Long id;
    private String descricao;
    private TipoCompromisso tipoCompromisso;
    private TipoTcc tipoTcc;
    private Long idAgenda;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

}
