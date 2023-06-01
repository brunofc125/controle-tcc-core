package com.controletcc.repository.projection;

import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;

public interface ModeloAvaliacaoValidateProjection {
    Long getId();

    TipoTcc getTipoTcc();

    TipoProfessor getTipoProfessor();
}
