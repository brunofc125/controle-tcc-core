package com.controletcc.model.enums;

import lombok.Getter;

@Getter
public enum SituacaoTcc {
    EM_ANDAMENTO("Em andamento"), A_DEFENDER("Á defender"), APROVADO("Aprovado"), REPROVADO("Reprovado"), CANCELADO("Cancelado");

    private SituacaoTcc(String descricao) {
        this.descricao = descricao;
    }

    private final String descricao;
}
