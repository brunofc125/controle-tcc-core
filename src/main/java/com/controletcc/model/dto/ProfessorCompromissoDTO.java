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
public class ProfessorCompromissoDTO extends BaseDTO {
    private Long id;
    private Long idProfessor;
    private String descricao;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
}
