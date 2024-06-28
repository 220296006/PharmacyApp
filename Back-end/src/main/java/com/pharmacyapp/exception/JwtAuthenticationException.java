package com.pharmacyapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final HttpStatus httpStatus;

    public JwtAuthenticationException(String message, Exception e, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
