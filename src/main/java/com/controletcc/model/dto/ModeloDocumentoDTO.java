package com.controletcc.model.dto;

import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModeloDocumentoDTO extends ArquivoDTO {
    private Long id;
    private String descricao;
    private String nome;
    private TipoTcc tipoTcc;
    private String base64Conteudo;
    private String mediaType;
}
