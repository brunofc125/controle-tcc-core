package com.controletcc.config.security;

import com.controletcc.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private Long id;
    private String name;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
        this.id = user.getId();
        this.name = user.getName();
    }
}
