package com.controletcc.dto;

import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeloAvaliacaoResumeDTO {
    private Long id;
    private Set<TipoTcc> tipoTccs;
    private Set<TipoProfessor> tipoProfessores;
}
