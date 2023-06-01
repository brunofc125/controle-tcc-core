package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
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
@Table(name = "modelo_aspecto_avaliacao")
public class ModeloAspectoAvaliacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo_item_avaliacao", nullable = false)
    private ModeloItemAvaliacao modeloItemAvaliacao;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "peso", nullable = false)
    private Long peso;

    public Long getIdModeloItemAvaliacao() {
        return this.modeloItemAvaliacao != null ? this.modeloItemAvaliacao.getId() : null;
    }

    public void setIdModeloItemAvaliacao(Long idModeloItemAvaliacao) {
        if (idModeloItemAvaliacao != null) {
            if (this.modeloItemAvaliacao == null) {
                this.modeloItemAvaliacao = new ModeloItemAvaliacao();
            }
            this.modeloItemAvaliacao.setId(idModeloItemAvaliacao);
        } else {
            this.modeloItemAvaliacao = null;
        }
    }

}
