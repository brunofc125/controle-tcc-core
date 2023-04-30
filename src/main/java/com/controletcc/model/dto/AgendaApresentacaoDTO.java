package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import com.controletcc.model.enums.TipoTcc;
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
public class AgendaApresentacaoDTO extends BaseDTO {
    private Long id;
    private String descricao;
    private TipoTcc tipoTcc;
    private Long idAreaTcc;
    private String anoPeriodo;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private Integer horaInicial;
    private Integer horaFinal;
    private List<AgendaApresentacaoRestricaoDTO> agendaApresentacaoRestricoes;
}
