package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agenda_apresentacao")
public class AgendaApresentacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "tipo_tcc", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTcc tipoTcc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area_tcc", nullable = false)
    private AreaTcc areaTcc;

    @Column(name = "data_inicial", nullable = false)
    private LocalDate dataInicial;

    @Column(name = "data_final", nullable = false)
    private LocalDate dataFinal;

    @Column(name = "hora_inicial", nullable = false)
    private Integer horaInicial;

    @Column(name = "hora_final", nullable = false)
    private Integer horaFinal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agendaApresentacao")
    private List<AgendaApresentacaoRestricao> agendaApresentacaoRestricoes;

    public Long getIdAreaTcc() {
        return this.areaTcc != null ? this.areaTcc.getId() : null;
    }

    public void setIdAreaTcc(Long idAreaTcc) {
        if (idAreaTcc != null) {
            if (this.areaTcc == null) {
                this.areaTcc = new AreaTcc();
            }
            this.areaTcc.setId(idAreaTcc);
        } else {
            this.areaTcc = null;
        }
    }

    public LocalDateTime getDataHoraInicial() {
        return this.dataInicial.atTime(this.horaInicial, 0);
    }

    public LocalDateTime getDataHoraFinal() {
        return this.dataFinal.atTime(this.horaFinal, 0);
    }

}
