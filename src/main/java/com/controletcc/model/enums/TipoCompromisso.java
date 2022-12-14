package com.controletcc.model.enums;

import lombok.Getter;

@Getter
public enum TipoCompromisso {
    COMPROMISSO_PESSOAL("Compromisso Pessoal"), APRESENTACAO("Apresentação");

    private TipoCompromisso(String descricao) {
        this.descricao = descricao;
    }

    private final String descricao;
}
