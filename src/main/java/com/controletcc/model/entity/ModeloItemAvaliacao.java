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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "modelo_item_avaliacao")
public class ModeloItemAvaliacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo_avaliacao", nullable = false)
    private ModeloAvaliacao modeloAvaliacao;

    @ElementCollection(targetClass = TipoTcc.class)
    @CollectionTable(name = "modelo_item_avaliacao_tipo_tcc", joinColumns = @JoinColumn(name = "id_modelo_item_avaliacao"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tcc", nullable = false)
    private Set<TipoTcc> tipoTccs;

    @ElementCollection(targetClass = TipoProfessor.class)
    @CollectionTable(name = "modelo_item_avaliacao_tipo_professor", joinColumns = @JoinColumn(name = "id_modelo_item_avaliacao"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_professor", nullable = false)
    private Set<TipoProfessor> tipoProfessores;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modeloItemAvaliacao")
    @OrderBy("id asc")
    private List<ModeloAspectoAvaliacao> modeloAspectosAvaliacao;

    @Column(name = "data_exclusao")
    private LocalDateTime dataExclusao;

    @Formula("(select string_agg(miatt.tipo_tcc, ', ' order by miatt.tipo_tcc) from modelo_item_avaliacao_tipo_tcc miatt where miatt.id_modelo_item_avaliacao = id)")
    private String tipoTccsNome;

    @Formula("(select string_agg(miatp.tipo_professor, ', ' order by miatp.tipo_professor) from modelo_item_avaliacao_tipo_professor miatp where miatp.id_modelo_item_avaliacao = id)")
    private String tipoProfessoresNome;

    public Long getIdModeloAvaliacao() {
        return this.modeloAvaliacao != null ? this.modeloAvaliacao.getId() : null;
    }

    public void setIdModeloAvaliacao(Long idModeloAvaliacao) {
        if (idModeloAvaliacao != null) {
            if (this.modeloAvaliacao == null) {
                this.modeloAvaliacao = new ModeloAvaliacao();
            }
            this.modeloAvaliacao.setId(idModeloAvaliacao);
        } else {
            this.modeloAvaliacao = null;
        }
    }


}
