package com.controletcc.repository.custom;

import com.controletcc.model.entity.base.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RepositoryCustomImpl<T extends BaseEntity> implements RepositoryCustom<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(T entity) {
        entityManager.detach(entity);
    }
}
