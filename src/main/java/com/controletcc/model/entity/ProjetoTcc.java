package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projeto_tcc")
public class ProjetoTcc extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tema", nullable = false)
    private String tema;

    @Column(name = "ano_periodo", nullable = false)
    private String anoPeriodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area_tcc", nullable = false)
    private AreaTcc areaTcc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_situacao_atual")
    private ProjetoTccSituacao situacaoAtual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor_orientador", nullable = false)
    private Professor professorOrientador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor_supervisor", nullable = false)
    private Professor professorSupervisor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "projeto_tcc_aluno", joinColumns = @JoinColumn(name = "id_projeto_tcc"), inverseJoinColumns = @JoinColumn(name = "id_aluno"))
    private List<Aluno> alunos;

    @Formula("""
                (select
                    string_agg(a.nome, ', ' order by a.nome)
                from aluno a
                join projeto_tcc_aluno pta on pta.id_aluno = a.id
                where pta.id_projeto_tcc = id)
            """)
    private String alunosNome;

    @Formula("""
                (select
                    string_agg(p.nome, ', ' order by p.nome)
                from projeto_tcc pt
                join projeto_tcc_situacao pts on pts.id = pt.id_situacao_atual
                join membro_banca mb on mb.id_projeto_tcc = pt.id and mb.tipo_tcc = pts.tipo_tcc
                join professor p on p.id = mb.id_professor
                where pt.id = id)
            """)
    private String membrosBancaNome;

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

    public Long getIdProfessorOrientador() {
        return this.professorOrientador != null ? this.professorOrientador.getId() : null;
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

    public Long getIdProfessorSupervisor() {
        return this.professorSupervisor != null ? this.professorSupervisor.getId() : null;
    }

    public void setIdProfessorSupervisor(Long idProfessorSupervisor) {
        if (idProfessorSupervisor != null) {
            if (this.professorSupervisor == null) {
                this.professorSupervisor = new Professor();
            }
            this.professorSupervisor.setId(idProfessorSupervisor);
        } else {
            this.professorSupervisor = null;
        }
    }

    public List<Long> getIdAlunoList() {
        return alunos != null && !alunos.isEmpty() ? alunos.stream().map(Aluno::getId).toList() : Collections.emptyList();
    }

    public void setIdAlunoList(List<Long> idAlunoList) {
        if (idAlunoList != null && !idAlunoList.isEmpty()) {
            this.alunos = idAlunoList.stream().map(id -> {
                var aluno = new Aluno();
                aluno.setId(id);
                return aluno;
            }).toList();
        } else {
            this.alunos = Collections.emptyList();
        }
    }

    public SituacaoTcc getSituacaoTcc() {
        return situacaoAtual != null ? situacaoAtual.getSituacaoTcc() : null;
    }

    public TipoTcc getTipoTcc() {
        return situacaoAtual != null ? situacaoAtual.getTipoTcc() : null;
    }

    public Integer getAno() {
        return StringUtil.isNullOrBlank(anoPeriodo) || !anoPeriodo.matches("\\d{4}/\\d") ? null : Integer.valueOf(anoPeriodo.substring(0, 4));
    }

    public Integer getPeriodo() {
        return StringUtil.isNullOrBlank(anoPeriodo) || !anoPeriodo.matches("\\d{4}/\\d") ? null : Integer.valueOf(anoPeriodo.substring(5));
    }

}
