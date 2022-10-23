package com.controletcc.repository.custom;

import com.controletcc.model.entity.base.BaseEntity;

public interface RepositoryCustom<T extends BaseEntity> {

    public void detach(T entity);
}
