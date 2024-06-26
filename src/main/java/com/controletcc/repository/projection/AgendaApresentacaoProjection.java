package com.controletcc.repository.projection;

import com.controletcc.model.enums.TipoTcc;

import java.time.LocalDate;

public interface AgendaApresentacaoProjection {
    Long getId();

    String getDescricao();

    TipoTcc getTipoTcc();

    String getDescricaoAreaTcc();

    String getAnoPeriodo();

    LocalDate getDataInicial();

    LocalDate getDataFinal();
}
