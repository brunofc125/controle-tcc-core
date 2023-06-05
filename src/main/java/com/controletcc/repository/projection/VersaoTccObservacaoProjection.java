package com.controletcc.repository.projection;

import java.time.LocalDateTime;

public interface VersaoTccObservacaoProjection {
    Long getId();

    String getProfessor();

    LocalDateTime getDataInclusao();

    String getNomeArquivo();

    String getObservacao();

    boolean isAvaliacao();
}
