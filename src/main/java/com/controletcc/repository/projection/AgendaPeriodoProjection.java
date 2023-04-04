package com.controletcc.repository.projection;

import java.time.LocalDate;

public interface AgendaPeriodoProjection {
    Integer getAno();

    Integer getPeriodo();

    LocalDate getMinDataInicial();

    LocalDate getMaxDataFinal();

    Integer getMinHoraInicial();

    Integer getMaxHoraFinal();
}
