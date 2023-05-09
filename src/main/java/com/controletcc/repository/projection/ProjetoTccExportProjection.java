package com.controletcc.repository.projection;

import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;

public interface ProjetoTccExportProjection {
    Long getId();

    String getTema();

    String getAlunos();

    String getProfessorOrientador();

    String getProfessorSupervisor();

    String getMembrosBanca();

    String getAnoPeriodo();

    TipoTcc getTipoTcc();

    SituacaoTcc getSituacaoTcc();
}
