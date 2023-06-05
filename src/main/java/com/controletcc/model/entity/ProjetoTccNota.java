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
@Table(name = "projeto_tcc_nota", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_projeto_tcc", "tipo_tcc"})})
public class ProjetoTccNota extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_tcc")
    private ProjetoTcc projetoTcc;

    @Column(name = "tipo_tcc", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTcc tipoTcc;

    @Column(name = "nota_media", nullable = false)
    private Double notaMedia;

    @Column(name = "nota_maxima", nullable = false)
    private Double notaMaxima;

    @Column(name = "nota_final")
    private Double notaFinal;

    @Column(name = "data_lancamento")
    private LocalDateTime dataLancamento;

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
