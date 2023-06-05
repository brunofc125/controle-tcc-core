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
@Table(name = "projeto_tcc_aspecto_avaliacao")
public class ProjetoTccAspectoAvaliacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_tcc_avaliacao", nullable = false, updatable = false)
    private ProjetoTccAvaliacao projetoTccAvaliacao;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "peso", nullable = false)
    private Long peso;

    @Column(name = "valor")
    private Double valor;

    public Long getIdProjetoTccAvaliacao() {
        return this.projetoTccAvaliacao != null ? this.projetoTccAvaliacao.getId() : null;
    }

    public void setIdProjetoTccAvaliacao(Long idProjetoTccAvaliacao) {
        if (idProjetoTccAvaliacao != null) {
            if (this.projetoTccAvaliacao == null) {
                this.projetoTccAvaliacao = new ProjetoTccAvaliacao();
            }
            this.projetoTccAvaliacao.setId(idProjetoTccAvaliacao);
        } else {
            this.projetoTccAvaliacao = null;
        }
    }

}
