package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccDTO extends BaseDTO {
    private Long id;
    private String tema;
    private String anoPeriodo;
    private Long idAreaTcc;
    private ProjetoTccSituacaoDTO situacaoAtual;
    private SituacaoTcc situacaoTcc;
    private TipoTcc tipoTcc;
    private Long idProfessorOrientador;
    private Long idProfessorSupervisor;
    private List<AlunoDTO> alunos;
    private Set<Long> docVisualizadoPor;
}
