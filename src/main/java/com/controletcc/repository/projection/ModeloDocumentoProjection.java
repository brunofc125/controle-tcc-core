package com.controletcc.repository.projection;

import com.controletcc.model.enums.TipoTcc;

public interface ModeloDocumentoProjection {
    Long getId();

    String getNome();

    String getDescricao();

    TipoTcc getTipoTcc();
}
