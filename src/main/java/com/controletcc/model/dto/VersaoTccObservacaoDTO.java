package com.controletcc.model.dto;

import com.controletcc.model.dto.base.ArquivoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VersaoTccObservacaoDTO extends ArquivoDTO {
    private Long id;
    private String observacao;
    private Long idVersaoTcc;
    private boolean avaliacao;
}
