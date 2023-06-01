package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeloAvaliacaoDTO extends BaseDTO {
    private Long id;
    private Long idAreaTcc;
    private Double notaMedia;
    private Double notaMaxima;
    private LocalDateTime dataExclusao;
}
