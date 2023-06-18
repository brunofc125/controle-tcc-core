package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeloAspectoAvaliacaoDTO extends BaseDTO {
    private Long id;
    private Long idModeloItemAvaliacao;
    private String descricao;
    private Long peso;
}
