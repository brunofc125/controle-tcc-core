package com.controletcc.model.enums;

import lombok.Getter;

@Getter
public enum TipoTcc {
    QUALIFICACAO("Qualificação"), DEFESA("Defesa");

    private TipoTcc(String descricao) {
        this.descricao = descricao;
    }

    private final String descricao;
}
