package com.controletcc.model.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class BaseDTO implements Serializable {
    private LocalDateTime dataInclusao;
    private LocalDateTime dataUltimaAlteracao;
}
