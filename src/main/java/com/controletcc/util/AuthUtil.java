package com.controletcc.util;

import com.controletcc.config.security.UserLogged;
import com.controletcc.model.enums.UserType;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    private AuthUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    public static Long getUserIdLogged() {
        var principal = AuthUtil.getPrincipal();
        return principal != null ? principal.getId() : 0L;
    }

    public static UserType getUserTypeLogged() {
        var principal = AuthUtil.getPrincipal();
        return principal != null ? principal.getType() : null;
    }

    public static String getUsernameLogged() {
        var principal = AuthUtil.getPrincipal();
        return principal != null ? principal.getName() : "SISTEMA";
    }

    public static UserLogged getPrincipal() {
        if (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null || !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserLogged)) {
            return null;
        }
        return (UserLogged) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
