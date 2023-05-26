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
@Table(name = "modelo_item_avaliacao")
public class ModeloItemAvaliacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo_avaliacao", nullable = false)
    private ModeloAvaliacao modeloAvaliacao;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "peso", nullable = false)
    private Long peso;

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
