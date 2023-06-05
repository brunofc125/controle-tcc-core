package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projeto_tcc_avaliacao")
public class ProjetoTccAvaliacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo_item_avaliacao", nullable = false)
    private ModeloItemAvaliacao modeloItemAvaliacao;

    @Column(name = "tipo_tcc", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTcc tipoTcc;

    @Column(name = "tipo_professor", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoProfessor tipoProfessor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_tcc", nullable = false)
    private ProjetoTcc projetoTcc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "projetoTccAvaliacao")
    @OrderBy("id asc")
    private List<ProjetoTccAspectoAvaliacao> projetoTccAspectosAvaliacao;

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

    public Long getIdProfessor() {
        return this.professor != null ? this.professor.getId() : null;
    }

    public void setIdProfessor(Long idProfessor) {
        if (idProfessor != null) {
            if (this.professor == null) {
                this.professor = new Professor();
            }
            this.professor.setId(idProfessor);
        } else {
            this.professor = null;
        }
    }

}
