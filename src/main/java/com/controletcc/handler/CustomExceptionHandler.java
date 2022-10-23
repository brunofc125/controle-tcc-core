package com.controletcc.handler;

import com.controletcc.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<String> handleBusinessException(BusinessException businessException) {
        return new ResponseEntity<>(businessException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<String> handleException(AccessDeniedException accessDeniedException) {
        return new ResponseEntity<>(accessDeniedException.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(Exception exception) {
        var uuid = UUID.randomUUID();
        logger.error("ERRO: " + uuid + " - " + exception.getMessage() + " - " + exception.getCause(), exception);
        return new ResponseEntity<>("Erro inesperado: " + uuid, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
