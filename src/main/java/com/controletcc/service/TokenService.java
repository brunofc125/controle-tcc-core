package com.controletcc.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.controletcc.config.security.CustomUserDetails;
import com.controletcc.config.security.SecurityConstants;
import com.controletcc.config.security.filter.CustomAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TokenService {

    private final UserDetailsService userDetailsService;

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                var refreshToken = authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
                var algorithm = Algorithm.HMAC256(SecurityConstants.SECRET.getBytes());
                var verifier = JWT.require(algorithm).build();
                var decodedJWT = verifier.verify(refreshToken);
                var username = decodedJWT.getSubject();
                var tokenMap = this.getTokenMap(request, username);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokenMap);
            } catch (Exception e) {
                CustomAuthorizationFilter.errorAuthenticationToken(response, e);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    public Map<String, String> getTokenMap(HttpServletRequest request, String username) {
        var user = getUserEnabled(username);
        var algorithm = Algorithm.HMAC256(SecurityConstants.SECRET.getBytes());
        var accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(SecurityConstants.CLAIM_NAME, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("id_user", user.getId())
                .withClaim("user_name", user.getName())
                .sign(algorithm);
        var newRefreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_EXPIRATION_TIME))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        var tokenMap = new HashMap<String, String>();
        tokenMap.put("access_token", accessToken);
        tokenMap.put("refresh_token", newRefreshToken);
        return tokenMap;
    }

    public CustomUserDetails getUserEnabled(String username) {
        var user = userDetailsService.loadUserByUsername(username);
        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        return (CustomUserDetails) user;
    }
}
