package com.fasttime.domain.member.controller;

import com.fasttime.global.util.ResponseDTO;
import java.util.HashMap;
import java.util.Map;
import javax.naming.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmailControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ResponseDTO> AuthenticationException(AuthenticationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
