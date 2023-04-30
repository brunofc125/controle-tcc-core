package com.controletcc.dto;

import com.controletcc.model.entity.Apresentacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgendaParaApresentacaoDTO implements Serializable {
    private Long id;
    private String descricao;
    private List<EventoDTO> outrasApresentacoes;
    private Map<String, ProfessorDisponibilidadeAgrupadaDTO> disponibilidades;

    public void setOutrasApresentacoes(List<Apresentacao> apresentacoes) {
        this.outrasApresentacoes = apresentacoes != null && !apresentacoes.isEmpty() ? apresentacoes.stream().map(EventoDTO::new).toList() : Collections.emptyList();
    }

}
