package com.controletcc.model.dto;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccAvaliacaoDTO extends BaseEntity {
    private Long id;
    private Long idModeloItemAvaliacao;
    private TipoTcc tipoTcc;
    private TipoProfessor tipoProfessor;
    private Long idProjetoTcc;
    private Long idProfessor;
    private List<ProjetoTccAspectoAvaliacaoDTO> projetoTccAspectosAvaliacao;
}
