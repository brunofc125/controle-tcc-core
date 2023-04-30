package com.controletcc.dto.options;

import com.controletcc.dto.options.base.BaseGridOptions;
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
public class AgendaApresentacaoGridOptions extends BaseGridOptions {

    private Long id;
    private String descricao;
    private TipoTcc tipoTcc;
    private Long idAreaTcc;
    private String anoPeriodo;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

}
