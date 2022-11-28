package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.SituacaoTcc;
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
public class ProjetoTccSituacao extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_tcc", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTcc tipoTcc;

    @Column(name = "situacao_tcc", nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoTcc situacaoTcc;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_tcc", nullable = false)
    private ProjetoTcc projetoTcc;

    @Column(name = "motivo")
    private String motivo;

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

}
