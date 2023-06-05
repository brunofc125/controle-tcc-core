package com.controletcc.repository.projection;

public interface AnexoGeralProjection {
    Long getId();

    String getDescricao();

    String getTipoTccsNome();

    String getModelo();

    String getProfessor();

    boolean isAvaliacao();
}
