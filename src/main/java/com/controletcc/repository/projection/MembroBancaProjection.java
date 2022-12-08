package com.controletcc.repository.projection;

import java.time.LocalDateTime;

public interface MembroBancaProjection {
    Long getId();

    String getNomeProfessor();

    LocalDateTime getDataSolicitacao();

    LocalDateTime getDataConfirmacao();

    boolean isConfirmado();
}
