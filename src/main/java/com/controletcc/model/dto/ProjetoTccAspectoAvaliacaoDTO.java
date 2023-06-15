package com.controletcc.model.dto;

import com.controletcc.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccAspectoAvaliacaoDTO extends BaseEntity {
    private Long id;
    private Long idProjetoTccAvaliacao;
    private Long idModeloAspectoAvaliacao;
    private String descricao;
    private Long peso;
    private Double valor;
}
