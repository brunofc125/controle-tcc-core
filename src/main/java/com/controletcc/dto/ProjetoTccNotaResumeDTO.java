package com.controletcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccNotaResumeDTO {
    private Long idProjetoTcc;
    private Double notaMedia;
    private Double notaMaxima;
    private Double notaFinal;
    private boolean avaliacaoFinalizada;
}
