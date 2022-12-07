package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agenda_apresentacao_restricao")
public class AgendaApresentacaoRestricao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agenda_apresentacao", nullable = false)
    private AgendaApresentacao agendaApresentacao;

    @Column(name = "data_inicial", nullable = false)
    private LocalDateTime dataInicial;

    @Column(name = "data_final", nullable = false)
    private LocalDateTime dataFinal;

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

}
