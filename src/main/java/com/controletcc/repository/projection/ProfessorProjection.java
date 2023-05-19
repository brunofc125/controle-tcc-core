package com.controletcc.repository.projection;

public interface ProfessorProjection {
    Long getId();

    String getNome();

    String getEmail();

    String getMatricula();

    boolean isSupervisorTcc();

    Long getIdUser();

    boolean isUserEnabled();
}
