package com.controletcc.repository.projection;

import java.time.LocalDateTime;

public interface ProfessorDisponibilidadeAgrupadaProjection {
    LocalDateTime getDataHora();

    String getDescricao();

    Long getQtdProfessores();
}
