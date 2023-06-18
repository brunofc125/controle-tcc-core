package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeloItemAvaliacaoDTO extends BaseDTO {
    private Long id;
    private Long idModeloAvaliacao;
    private Set<TipoTcc> tipoTccs;
    private Set<TipoProfessor> tipoProfessores;
    private List<ModeloAspectoAvaliacaoDTO> modeloAspectosAvaliacao;
    private LocalDateTime dataExclusao;
}
