package com.controletcc.model.entity;

import com.controletcc.model.entity.base.ArquivoOpcional;
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
@Table(name = "versao_tcc_observacao")
public class VersaoTccObservacao extends ArquivoOpcional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "observacao", length = 4000)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_versao_tcc", nullable = false)
    private VersaoTcc versaoTcc;

    @Column(name = "avaliacao")
    private boolean avaliacao;

    public Long getIdVersaoTcc() {
        return versaoTcc != null ? versaoTcc.getId() : null;
    }

    public void setIdVersaoTcc(Long idVersaoTcc) {
        if (idVersaoTcc != null) {
            if (this.versaoTcc == null) {
                this.versaoTcc = new VersaoTcc();
            }
            this.versaoTcc.setId(idVersaoTcc);
        } else {
            this.versaoTcc = null;
        }
    }

}
