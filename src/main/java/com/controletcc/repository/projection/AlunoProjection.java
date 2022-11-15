package com.controletcc.repository.projection;

public interface AlunoProjection {
    Long getId();

    String getNome();

    String getCpf();

    String getEmail();

    String getMatricula();

    String getDescricaoAreaTcc();

    Long getIdUser();

    boolean isUserEnabled();
}
