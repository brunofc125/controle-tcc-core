package com.controletcc.config.security;

import com.controletcc.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLogged {
    private Long id;
    private String name;
    private UserType type;

    @Override
    public String toString() {
        return this.name;
    }
}
