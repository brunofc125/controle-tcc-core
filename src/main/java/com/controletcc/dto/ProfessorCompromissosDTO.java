package com.controletcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorCompromissosDTO implements Serializable {
    private List<EventoDTO> outrosCompromissos;
    private List<EventoDTO> apresentacoes;

//    public void setOutrosCompromissos(List<ProfessorDisponibilidadeProjection> compromissos) {
//        this.outrosCompromissos = compromissos != null && !compromissos.isEmpty() ? compromissos.stream().map(EventoDTO::new).toList() : Collections.emptyList();
//    }
//
//    public void setApresentacoes(List<ProfessorDisponibilidadeProjection> apresentacoes) {
//        this.apresentacoes = apresentacoes != null && !apresentacoes.isEmpty() ? apresentacoes.stream().map(EventoDTO::new).toList() : Collections.emptyList();
//    }
}
