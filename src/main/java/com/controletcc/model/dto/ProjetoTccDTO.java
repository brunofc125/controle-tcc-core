package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private Long idProfessorOrientador;
    private Long idProfessorSupervisor;
    private List<Long> idAlunoList;
}
