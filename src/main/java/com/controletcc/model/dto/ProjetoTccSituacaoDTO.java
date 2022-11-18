package com.controletcc.model.dto;

import com.controletcc.model.dto.base.BaseDTO;
import com.controletcc.model.enums.SituacaoTcc;
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
public class ProjetoTccSituacaoDTO extends BaseDTO {
    private Long id;
    private TipoTcc tipoTcc;
    private SituacaoTcc situacaoTcc;
    private LocalDateTime dataConclusao;
    private Long idProjetoTcc;
}
