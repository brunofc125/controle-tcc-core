package com.controletcc.model.dto;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccNotaDTO extends BaseEntity {
    private Long id;
    private Long idProjetoTcc;
    private TipoTcc tipoTcc;
    private Double notaMedia;
    private Double notaMaxima;
    private Double notaFinal;
    private LocalDateTime dataLancamento;
}
