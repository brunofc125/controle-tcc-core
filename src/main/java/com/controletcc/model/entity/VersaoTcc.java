package com.controletcc.model.entity;

import com.controletcc.model.entity.base.Arquivo;
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
@Table(name = "versao_tcc", uniqueConstraints = {@UniqueConstraint(columnNames = {"versao", "id_projeto_tcc"})})
public class VersaoTcc extends Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "observacao", length = 1000)
    private String observacao;

    @Column(name = "versao", nullable = false)
    private Long versao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_tcc", nullable = false)
    private ProjetoTcc projetoTcc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor_orientador")
    private Professor professorOrientador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

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

    public Long getIdProfessorOrientador() {
        return professorOrientador != null ? professorOrientador.getId() : null;
    }

    public void setIdProfessorOrientador(Long idProfessorOrientador) {
        if (idProfessorOrientador != null) {
            if (this.professorOrientador == null) {
                this.professorOrientador = new Professor();
            }
            this.professorOrientador.setId(idProfessorOrientador);
        } else {
            this.professorOrientador = null;
        }
    }

    public Long getIdAluno() {
        return aluno != null ? aluno.getId() : null;
    }

    public void setIdAluno(Long idAluno) {
        if (idAluno != null) {
            if (this.aluno == null) {
                this.aluno = new Aluno();
            }
            this.aluno.setId(idAluno);
        } else {
            this.aluno = null;
        }
    }

}
