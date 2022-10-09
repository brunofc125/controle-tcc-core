package com.controletcc.service;

import com.controletcc.model.entity.base.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public class BaseService<T extends BaseEntity, ID, R extends JpaRepository<T, ID>> {

    private final R repository;

    public BaseService(R repository) {
        this.repository = repository;
    }

    public T insert(T entity) {
        entity.setDataInclusao(LocalDateTime.now());
        return this.repository.save(entity);
    }

    public T update(T entity) {
        entity.setDataUltimaAlteracao(LocalDateTime.now());
        return this.repository.save(entity);
    }
}
