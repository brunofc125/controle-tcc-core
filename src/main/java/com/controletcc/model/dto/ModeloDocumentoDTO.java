package com.controletcc.model.dto;

import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModeloDocumentoDTO extends ArquivoDTO {
    private Long id;
    private String descricao;
    private String nome;
    private Set<TipoTcc> tipoTccs;
}
