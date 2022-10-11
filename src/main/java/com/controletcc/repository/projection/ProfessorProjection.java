package com.controletcc.repository.projection;

public interface ProfessorProjection {
    Long getId();

    String getNome();

    String getCpf();

    String getEmail();

    boolean isSupervisorTcc();
}
