package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
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
@Table(name = "professor_compromisso")
public class ProfessorCompromisso extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "data_inicial", nullable = false)
    private LocalDateTime dataInicial;

    @Column(name = "data_final", nullable = false)
    private LocalDateTime dataFinal;

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
