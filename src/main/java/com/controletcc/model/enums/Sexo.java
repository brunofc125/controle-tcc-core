package com.controletcc.model.enums;

import lombok.Getter;

@Getter
public enum Sexo {
    MASC("Masculino"), FEM("Feminino"), INDF("Indefinido");

    private Sexo(String descricao) {
        this.descricao = descricao;
    }

    private final String descricao;
}
