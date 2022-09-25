package com.controletcc.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.controletcc.config.security.SecurityConstants;
import com.controletcc.model.Role;
import com.controletcc.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/token")
@RequiredArgsConstructor
public class TokenController {

    private final UserService userService;

    @GetMapping("refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                var refreshToken = authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
                var algorithm = Algorithm.HMAC256(SecurityConstants.SECRET.getBytes());
                var verifier = JWT.require(algorithm).build();
                var decodedJWT = verifier.verify(refreshToken);
                var username = decodedJWT.getSubject();
                var user = userService.getUser(username);
                var accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim(SecurityConstants.CLAIM_NAME, user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                var tokenMap = new HashMap<String, String>();
                tokenMap.put("access_token", accessToken);
                tokenMap.put("refresh_token", refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokenMap);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                var errorMap = new HashMap<String, String>();
                errorMap.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errorMap);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

}
