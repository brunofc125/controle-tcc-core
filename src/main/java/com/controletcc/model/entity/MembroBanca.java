package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.TipoTcc;
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
@Table(name = "membro_banca", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_projeto_tcc", "id_professor", "tipo_tcc"})})
public class MembroBanca extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_tcc", nullable = false)
    private ProjetoTcc projetoTcc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @Column(name = "tipo_tcc", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTcc tipoTcc;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

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
