package com.controletcc.repository.projection;

import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;

import java.time.LocalDateTime;

public interface ProjetoTccProjection {
    Long getId();

    String getTema();

    String getAlunos();

    String getProfessorOrientador();

    String getProfessorSupervisor();

    String getAnoPeriodo();

    TipoTcc getTipoTcc();

    LocalDateTime getDataSolicitacaoBanca();

    LocalDateTime getDataConfirmacaoBanca();

    SituacaoTcc getSituacaoTcc();

    boolean isApresentacaoAgendada();

    boolean isAvaliacaoIniciada();
}
