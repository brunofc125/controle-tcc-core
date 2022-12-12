package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.TipoTcc;
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
@Table(name = "apresentacao", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_projeto_tcc", "tipo_tcc"})})
public class Apresentacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agenda_apresentacao", nullable = false)
    private AgendaApresentacao agendaApresentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_tcc", nullable = false)
    private ProjetoTcc projetoTcc;

    @Column(name = "tipo_tcc", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTcc tipoTcc;

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

    public Long getIdProjetoTcc() {
        return this.projetoTcc != null ? this.projetoTcc.getId() : null;
    }

    public void setIdProjetoTcc(Long idProjetoTcc) {
        if (idProjetoTcc != null) {
            if (this.projetoTcc == null) {
                this.projetoTcc = new ProjetoTcc();
            }
            this.projetoTcc.setId(idProjetoTcc);
        } else {
            this.projetoTcc = null;
        }
    }

}
