package com.controletcc.model.entity.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseModel {
    @Column(name = "data_inclusao", updatable = false)
    protected LocalDateTime dataInclusao;

    @Column(name = "data_ultima_alteracao", insertable = false)
    protected LocalDateTime dataUltimaAlteracao;
}
