package com.controletcc.model.enums;

import lombok.Getter;

@Getter
public enum TipoProfessor {
    SUPERVISOR("Supervisor"),
    ORIENTADOR("Orientador"),
    MEMBRO_BANCA("Membro de Banca");

    private TipoProfessor(String descricao) {
        this.descricao = descricao;
    }

    private final String descricao;
}
