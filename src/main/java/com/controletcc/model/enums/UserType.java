package com.controletcc.model.enums;

import lombok.Getter;

@Getter
public enum UserType {
    ADMIN("Administrador"), SUPERVISOR("Supervisor"), PROFESSOR("Professor"), ALUNO("Aluno");

    private UserType(String descricao) {
        this.descricao = descricao;
    }

    private final String descricao;
}
