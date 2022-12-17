package com.controletcc.repository.projection;

import com.controletcc.model.enums.TipoCompromisso;
import com.controletcc.model.enums.TipoTcc;

import java.time.LocalDateTime;

public interface ProfessorDisponibilidadeProjection {
    Long getId();

    TipoCompromisso getTipoCompromisso();

    TipoTcc getTipoTcc();

    String getDescricao();

    String getAlunosNome();

    LocalDateTime getDataInicial();

    LocalDateTime getDataFinal();

    String getPapel();
}
