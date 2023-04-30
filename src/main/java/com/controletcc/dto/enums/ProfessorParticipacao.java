package com.controletcc.dto.enums;

import lombok.Getter;

@Getter
public enum ProfessorParticipacao {
    SUPERVISOR("Supervisor"),
    ORIENTADOR("Orientador"),
    MEMBRO_BANCA("Membro de Banca");

    private ProfessorParticipacao(String descricao) {
        this.descricao = descricao;
    }

    private final String descricao;
}
