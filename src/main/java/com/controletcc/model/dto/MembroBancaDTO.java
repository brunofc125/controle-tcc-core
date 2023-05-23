package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
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
public class MembroBancaDTO extends BaseDTO {
    private Long id;
    private Long idProjetoTcc;
    private Long idProfessor;
    private TipoTcc tipoTcc;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataConfirmacao;
}
