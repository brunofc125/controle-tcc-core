package com.controletcc.model.entity;

import com.controletcc.model.entity.base.Arquivo;
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
@Table(name = "anexo_geral")
public class AnexoGeral extends Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo_documento")
    private ModeloDocumento modeloDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_tcc", nullable = false)
    private ProjetoTcc projetoTcc;

    @Column(name = "data_exclusao")
    private LocalDateTime dataExclusao;

    @Column(name = "avaliacao")
    private boolean avaliacao;

    public Long getIdModeloDocumento() {
        return modeloDocumento != null ? modeloDocumento.getId() : null;
    }

    public void setIdModeloDocumento(Long idModeloDocumento) {
        if (idModeloDocumento != null) {
            if (this.modeloDocumento == null) {
                this.modeloDocumento = new ModeloDocumento();
            }
            this.modeloDocumento.setId(idModeloDocumento);
        } else {
            this.modeloDocumento = null;
        }
    }

    public Long getIdProfessor() {
        return professor != null ? professor.getId() : null;
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

    public Long getIdProjetoTcc() {
        return projetoTcc != null ? projetoTcc.getId() : null;
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
