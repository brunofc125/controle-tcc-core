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
@Table(name = "modelo_avaliacao")
public class ModeloAvaliacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area_tcc", nullable = false)
    private AreaTcc areaTcc;

    @Column(name = "nota_media", nullable = false)
    private Double notaMedia;

    @Column(name = "nota_maxima", nullable = false)
    private Double notaMaxima;

    @Column(name = "data_exclusao")
    private LocalDateTime dataExclusao;

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

}
