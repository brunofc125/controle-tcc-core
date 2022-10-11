package com.controletcc.config.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.controletcc.config.security.SecurityConstants;
import com.controletcc.config.security.UserLogged;
import com.controletcc.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(SecurityConstants.API_LOGIN) || request.getServletPath().equals(SecurityConstants.API_REFRESH_TOKEN)) {
            filterChain.doFilter(request, response);
        } else {
            var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                try {
                    var token = authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
                    var algorithm = Algorithm.HMAC256(SecurityConstants.SECRET.getBytes());
                    var verifier = JWT.require(algorithm).build();
                    var decodedJWT = verifier.verify(token);
                    var username = decodedJWT.getSubject();
                    var user = tokenService.getUserEnabled(username);
                    var userLogged = new UserLogged(user.getId(), user.getName(), user.getType());
                    var authenticationToken = new UsernamePasswordAuthenticationToken(userLogged, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    errorAuthenticationToken(response, e);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    public static void errorAuthenticationToken(HttpServletResponse response, Exception e) throws IOException {
        log.error("Error logging in: {}", e.getMessage());
        response.setHeader("error", e.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        var errorMap = new HashMap<String, String>();
        errorMap.put("error_message", e.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), errorMap);
    }
}
