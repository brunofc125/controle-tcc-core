package com.controletcc.model.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum SituacaoTcc {
    EM_ANDAMENTO("Em andamento", false),
    A_APRESENTAR("Á apresentar", false, EM_ANDAMENTO),
    EM_AVALIACAO("Em avaliação", false, A_APRESENTAR),
    APROVADO("Aprovado", true, EM_AVALIACAO),
    REPROVADO("Reprovado", true, EM_ANDAMENTO, A_APRESENTAR, EM_AVALIACAO),
    CANCELADO("Cancelado", true, EM_ANDAMENTO, A_APRESENTAR, EM_AVALIACAO);

    private SituacaoTcc(String descricao, boolean situacaoFinal, SituacaoTcc... situacoesAnteriores) {
        this.descricao = descricao;
        this.situacaoFinal = situacaoFinal;
        this.situacoesAnteriores = List.of(situacoesAnteriores);
    }

    private final String descricao;
    private final boolean situacaoFinal;
    private final List<SituacaoTcc> situacoesAnteriores;

    public boolean canUpdateTo(SituacaoTcc newSituacao) {
        if (this.equals(A_APRESENTAR) && EM_ANDAMENTO.equals(newSituacao)) {
            return true;
        }
        return newSituacao.situacoesAnteriores.contains(this);
    }

}
