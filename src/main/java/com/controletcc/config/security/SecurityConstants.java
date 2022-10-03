package com.controletcc.config.security;

public class SecurityConstants {

    public static final String SECRET = "SECRET_KEY";
    public static final int EXPIRATION_TIME = 600_000;
    public static final int REFRESH_EXPIRATION_TIME = 86_400_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String API_LOGIN = "/api/login";
    public static final String API_LOGIN_ALL = API_LOGIN + "/**";
    public static final String API_REFRESH_TOKEN = "/api/token/refresh";
    public static final String API_REFRESH_TOKEN_ALL = API_REFRESH_TOKEN + "/**";
    public static final String CLAIM_NAME = "roles";
}
