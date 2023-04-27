package com.controletcc.repository.projection;

import com.controletcc.model.enums.TipoTcc;

import java.time.LocalDateTime;

public interface ApresentacaoProjection {
    Long getId();

    String getDescricao();

    Long getIdProjetoTcc();

    TipoTcc getTipoTcc();

    LocalDateTime getDataInicial();

    LocalDateTime getDataFinal();
}
