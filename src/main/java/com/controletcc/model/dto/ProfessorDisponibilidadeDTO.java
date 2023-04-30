package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorDisponibilidadeDTO extends BaseDTO {
    private Long id;
    private Long idProfessor;
    private String anoPeriodo;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
}
