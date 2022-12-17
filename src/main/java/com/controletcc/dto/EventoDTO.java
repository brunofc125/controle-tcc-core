package com.controletcc.dto;

import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import com.controletcc.model.entity.Apresentacao;
import com.controletcc.model.enums.TipoCompromisso;
import com.controletcc.repository.projection.ProfessorDisponibilidadeProjection;
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

    public EventoDTO(ProfessorDisponibilidadeProjection compromisso) {
        this.id = compromisso.getId();
        var descricao = LocalDateTimeUtil.getHoursTitle(compromisso.getDataInicial(), compromisso.getDataFinal());
        if (TipoCompromisso.APRESENTACAO.equals(compromisso.getTipoCompromisso())) {
            descricao += ": " + compromisso.getAlunosNome();
        } else {
            descricao += ": " + compromisso.getDescricao();
        }
        this.descricao = descricao;
        this.start = compromisso.getDataInicial();
        this.end = compromisso.getDataFinal();
    }
}
