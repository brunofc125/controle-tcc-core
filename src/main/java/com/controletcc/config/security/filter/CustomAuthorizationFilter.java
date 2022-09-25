package com.controletcc.config.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.controletcc.config.security.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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
                    var roles = decodedJWT.getClaim(SecurityConstants.CLAIM_NAME).asArray(String.class);
                    var authorities = stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.error("Error logging in: {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
//                    response.sendError(HttpStatus.FORBIDDEN.value());
                    var errorMap = new HashMap<String, String>();
                    errorMap.put("error_message", e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), errorMap);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
