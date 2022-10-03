package com.controletcc.dto.enums;

import lombok.Getter;

@Getter
public enum OrderByDirection {
    ASC("asc"), DESC("desc");

    OrderByDirection(String descricao) {
        this.descricao = descricao;
    }

    private final String descricao;

}
