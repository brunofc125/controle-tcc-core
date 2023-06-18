package com.controletcc.dto.enums;

import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.UserType;
import lombok.Getter;

import java.util.Arrays;
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

    public TipoProfessor getTipoProfessor() {
        return switch (this) {
            case SUPERVISOR -> TipoProfessor.SUPERVISOR;
            case ORIENTADOR -> TipoProfessor.ORIENTADOR;
            case MEMBRO_BANCA -> TipoProfessor.MEMBRO_BANCA;
            case ALUNO -> null;
        };
    }
    
    public static boolean isProfessor(TccRoute tccRoute) {
        var tccRouteProf = Arrays.asList(TccRoute.SUPERVISOR, TccRoute.ORIENTADOR, TccRoute.MEMBRO_BANCA);
        return tccRoute != null && tccRouteProf.contains(tccRoute);
    }
}
