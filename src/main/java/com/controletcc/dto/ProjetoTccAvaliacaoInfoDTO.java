package com.controletcc.dto;

import com.controletcc.model.entity.ProjetoTccNota;
import com.controletcc.model.enums.TipoTcc;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccAvaliacaoInfoDTO implements Serializable {
    private Long idProjetoTcc;
    private String tema;
    private TipoTcc tipoTcc;
    private String anoPeriodo;
    private DescriptionModelDTO areaTcc;
    private DescriptionModelDTO orientador;
    private DescriptionModelDTO supervisor;
    private List<DescriptionModelDTO> membroBancaList;
    private List<DescriptionModelDTO> alunos;
    private Double notaFinal;
    private String situacaoAluno;

    public void setUpNotaFinalAndSituacaoAluno(@NonNull ProjetoTccNota projetoTccNota) {
        this.notaFinal = projetoTccNota.getNotaFinal();
        if (this.notaFinal != null) {
            var aprovado = this.notaFinal.compareTo(projetoTccNota.getNotaMedia()) >= 0;
            this.situacaoAluno = (aprovado ? "Aprovado, nota final >= " : "Reprovado, nota final < ") + projetoTccNota.getNotaMedia();
        }
    }
}
