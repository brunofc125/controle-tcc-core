package com.controletcc.repository.projection;

import java.time.LocalDateTime;

public interface ProfessorDisponibilidadeAgrupadaProjection {
    LocalDateTime getDataHora();

    String getIdsProfessores();

    Long getQtdProfessores();
}
