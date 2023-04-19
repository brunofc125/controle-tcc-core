package com.controletcc.dto;

import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import com.controletcc.model.entity.Apresentacao;
import com.controletcc.model.entity.ProfessorDisponibilidade;
import com.controletcc.util.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventoDTO implements Serializable {
    private Long id;
    private String descricao;
    private LocalDateTime start;
    private LocalDateTime end;

    public EventoDTO(AgendaApresentacaoRestricao restricao) {
        this.id = restricao.getId();
        this.descricao = LocalDateTimeUtil.getHoursTitle(restricao.getDataInicial(), restricao.getDataFinal());
        this.start = restricao.getDataInicial();
        this.end = restricao.getDataFinal();
    }

    public EventoDTO(Apresentacao apresentacao) {
        this.id = apresentacao.getId();
        this.descricao = LocalDateTimeUtil.getHoursTitle(apresentacao.getDataInicial(), apresentacao.getDataFinal()) + ": Outra Apresentação";
        this.start = apresentacao.getDataInicial();
        this.end = apresentacao.getDataFinal();
    }

    public EventoDTO(ProfessorDisponibilidade disponibilidade) {
        this.id = disponibilidade.getId();
        this.descricao = disponibilidade.getProfessor() != null ? disponibilidade.getProfessor().getNome() : null;
        this.start = disponibilidade.getDataInicial();
        this.end = disponibilidade.getDataFinal();
    }
}
