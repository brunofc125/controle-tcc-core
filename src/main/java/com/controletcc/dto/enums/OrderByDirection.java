package com.controletcc.dto.enums;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum OrderByDirection {
    ASC(Sort.Direction.ASC, "asc"), DESC(Sort.Direction.DESC, "desc");

    OrderByDirection(Sort.Direction direction, String descricao) {
        this.direction = direction;
        this.descricao = descricao;
    }

    private final Sort.Direction direction;
    private final String descricao;

}
