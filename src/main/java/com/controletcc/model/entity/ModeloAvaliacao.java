package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(targetClass = TipoTcc.class)
    @CollectionTable(name = "modelo_avaliacao_tipo_tcc", joinColumns = @JoinColumn(name = "id_modelo_avaliacao"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tcc", nullable = false)
    private Set<TipoTcc> tipoTccs;

    @ElementCollection(targetClass = TipoProfessor.class)
    @CollectionTable(name = "modelo_avaliacao_tipo_professor", joinColumns = @JoinColumn(name = "id_modelo_avaliacao"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_professor", nullable = false)
    private Set<TipoProfessor> tipoProfessores;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modeloAvaliacao")
    private List<ModeloItemAvaliacao> modeloItensAvaliacao;

    @Formula("(select string_agg(matt.tipo_tcc, ', ' order by matt.tipo_tcc) from modelo_avaliacao_tipo_tcc matt where matt.id_modelo_avaliacao = id)")
    private String tipoTccsNome;

    @Formula("(select string_agg(matp.tipo_professor, ', ' order by matp.tipo_professor) from modelo_avaliacao_tipo_professor matp where matp.id_modelo_avaliacao = id)")
    private String tipoProfessoresNome;

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
