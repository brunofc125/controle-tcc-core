package com.controletcc.repository.projection;

import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;

public interface ProjetoTccProjection {
    Long getId();

    String getTema();

    String getAlunos();

    String getProfessorOrientador();

    String getProfessorSupervisor();

    String getAnoPeriodo();

    TipoTcc getTipoTcc();

    SituacaoTcc getSituacaoTcc();

}
