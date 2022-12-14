package com.controletcc.model.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum SituacaoTcc {
    EM_ANDAMENTO("Em andamento", false),
    A_APRESENTAR("Á apresentar", false, EM_ANDAMENTO),
    APROVADO("Aprovado", true, A_APRESENTAR),
    REPROVADO("Reprovado", true, EM_ANDAMENTO, A_APRESENTAR),
    CANCELADO("Cancelado", true, EM_ANDAMENTO, A_APRESENTAR);

    private SituacaoTcc(String descricao, boolean situacaoFinal, SituacaoTcc... situacoesAnteriores) {
        this.descricao = descricao;
        this.situacaoFinal = situacaoFinal;
        this.situacoesAnteriores = List.of(situacoesAnteriores);
    }

    private final String descricao;
    private final boolean situacaoFinal;
    private final List<SituacaoTcc> situacoesAnteriores;

    public boolean canUpdateTo(SituacaoTcc newSituacao) {
        return newSituacao.situacoesAnteriores.contains(this);
    }

}
