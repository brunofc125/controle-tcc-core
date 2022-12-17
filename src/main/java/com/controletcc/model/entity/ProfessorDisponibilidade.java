package com.controletcc.model.entity;

import com.controletcc.model.entity.base.EventTime;
import com.controletcc.util.StringUtil;
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
@Table(name = "professor_disponibilidade")
public class ProfessorDisponibilidade extends EventTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "periodo", nullable = false)
    private Integer periodo;

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

    public String getAnoPeriodo() {
        return ano != null && periodo != null ? ano + "/" + periodo : null;
    }

    public void setAnoPeriodo(String anoPeriodo) {
        if (!StringUtil.isNullOrBlank(anoPeriodo) && anoPeriodo.matches("\\d{4}/\\d")) {
            var ano = anoPeriodo.substring(0, 4);
            var periodo = anoPeriodo.substring(5);
            this.ano = Integer.valueOf(ano);
            this.periodo = Integer.valueOf(periodo);
        } else {
            this.ano = null;
            this.periodo = null;
        }
    }

}
