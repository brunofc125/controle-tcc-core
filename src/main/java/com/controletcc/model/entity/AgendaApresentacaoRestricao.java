package com.controletcc.model.entity;

import com.controletcc.model.entity.base.EventTime;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agenda_apresentacao_restricao")
public class AgendaApresentacaoRestricao extends EventTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agenda_apresentacao", nullable = false)
    private AgendaApresentacao agendaApresentacao;

    public Long getIdAgendaApresentacao() {
        return this.agendaApresentacao != null ? this.agendaApresentacao.getId() : null;
    }

    public void setIdAgendaApresentacao(Long idAgendaApresentacao) {
        if (idAgendaApresentacao != null) {
            if (this.agendaApresentacao == null) {
                this.agendaApresentacao = new AgendaApresentacao();
            }
            this.agendaApresentacao.setId(idAgendaApresentacao);
        } else {
            this.agendaApresentacao = null;
        }
    }

    public TipoTcc getTipoTcc() {
        return this.agendaApresentacao != null ? this.agendaApresentacao.getTipoTcc() : null;
    }

}
