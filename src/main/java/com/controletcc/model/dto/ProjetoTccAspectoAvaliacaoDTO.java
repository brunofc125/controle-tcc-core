package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccAspectoAvaliacaoDTO extends BaseDTO {
    private Long id;
    private Long idProjetoTccAvaliacao;
    private Long idModeloAspectoAvaliacao;
    private String descricao;
    private Long peso;
    private Double valor;
}
