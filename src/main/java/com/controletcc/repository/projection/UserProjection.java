package com.controletcc.repository.projection;

import com.controletcc.model.enums.UserType;

public interface UserProjection {
    Long getId();

    UserType getType();

    String getName();

    String getUsername();

    boolean isEnabled();
}
