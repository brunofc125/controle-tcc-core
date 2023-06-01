package com.controletcc.model.dto;

import com.controletcc.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeloAspectoAvaliacaoDTO extends BaseEntity {
    private Long id;
    private Long idModeloItemAvaliacao;
    private String descricao;
    private Long peso;
}
