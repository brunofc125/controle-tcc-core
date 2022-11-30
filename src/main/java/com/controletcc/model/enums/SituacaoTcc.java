package com.controletcc.model.enums;

import lombok.Getter;

@Getter
public enum SituacaoTcc {
    EM_ANDAMENTO("Em andamento", false),
    A_DEFENDER("Á defender", false),
    APROVADO("Aprovado", true),
    REPROVADO("Reprovado", true),
    CANCELADO("Cancelado", true);

    private SituacaoTcc(String descricao, boolean situacaoFinal) {
        this.descricao = descricao;
        this.situacaoFinal = situacaoFinal;
    }

    private final String descricao;
    private final boolean situacaoFinal;
}
