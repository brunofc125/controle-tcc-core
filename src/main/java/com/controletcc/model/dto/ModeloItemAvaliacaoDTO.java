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
public class ModeloItemAvaliacaoDTO extends BaseEntity {
    private Long id;
    private Long idModeloAvaliacao;
    private String descricao;
    private Long peso;
}
