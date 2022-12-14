package com.controletcc.dto;

import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import com.controletcc.model.entity.Apresentacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgendaParaApresentacaoDTO implements Serializable {
    private Long id;
    private String descricao;
    private List<EventoDTO> agendaRestricoes;
    private List<EventoDTO> outrasApresentacoes;
    private List<EventoDTO> professorCompromisso;

    public void setAgendaRestricoes(List<AgendaApresentacaoRestricao> restricoes) {
        this.agendaRestricoes = restricoes != null && !restricoes.isEmpty() ? restricoes.stream().map(EventoDTO::new).toList() : Collections.emptyList();
    }

    public void setOutrasApresentacoes(List<Apresentacao> apresentacoes) {
        this.outrasApresentacoes = apresentacoes != null && !apresentacoes.isEmpty() ? apresentacoes.stream().map(EventoDTO::new).toList() : Collections.emptyList();
    }

}
