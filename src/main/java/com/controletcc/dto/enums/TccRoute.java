package com.controletcc.dto.enums;

import com.controletcc.model.enums.UserType;
import lombok.Getter;

import java.util.List;

@Getter
public enum TccRoute {
    SUPERVISOR("Supervisor", UserType.SUPERVISOR),
    ORIENTADOR("Orientador", UserType.SUPERVISOR, UserType.PROFESSOR),
    MEMBRO_BANCA("Membro de Banca", UserType.SUPERVISOR, UserType.PROFESSOR),
    ALUNO("Aluno", UserType.ALUNO);

    private TccRoute(String descricao, UserType... userTypesMatch) {
        this.descricao = descricao;
        this.userTypesMatch = List.of(userTypesMatch);
    }

    private final String descricao;
    private final List<UserType> userTypesMatch;

    public boolean userTypeMatches(UserType userType) {
        return this.userTypesMatch.contains(userType);
    }
}
