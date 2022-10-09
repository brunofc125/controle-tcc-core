package com.controletcc.model.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Column(name = "data_inclusao", updatable = false)
    private LocalDateTime dataInclusao = LocalDateTime.now();

    @Column(name = "data_ultima_alteracao", insertable = false)
    private LocalDateTime dataUltimaAlteracao = LocalDateTime.now();
}
