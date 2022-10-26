package com.controletcc.model.dto.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArquivoDTO extends BaseDTO {
    protected String base64Conteudo;
    protected String nomeArquivo;
    protected String mediaType;
}
