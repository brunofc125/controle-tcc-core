package com.controletcc.model.entity.base;

import com.controletcc.util.AuthUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Column(name = "id_user_inclusao", nullable = false, updatable = false)
    private Long idUserInclusao;

    @Column(name = "data_inclusao", nullable = false, updatable = false)
    private LocalDateTime dataInclusao;

    @Column(name = "id_user_ultima_alteracao", insertable = false)
    private Long idUserUltimaAlteracao;

    @Column(name = "data_ultima_alteracao", insertable = false)
    private LocalDateTime dataUltimaAlteracao;

    @PrePersist
    public void prePersist() {
        idUserInclusao = AuthUtil.getUserIdLogged();
        dataInclusao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        idUserUltimaAlteracao = AuthUtil.getUserIdLogged();
        dataUltimaAlteracao = LocalDateTime.now();
    }
}
