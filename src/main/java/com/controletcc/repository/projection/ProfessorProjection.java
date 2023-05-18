package com.controletcc.repository.projection;

public interface ProfessorProjection {
    Long getId();

    String getNome();

    String getEmail();

    boolean isSupervisorTcc();

    Long getIdUser();

    boolean isUserEnabled();
}
