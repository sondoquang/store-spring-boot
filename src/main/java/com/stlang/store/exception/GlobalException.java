package com.stlang.store.exception;

import com.stlang.store.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleDataNotFoundException(DataNotFoundException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        restResponse.setErrorMessage(e.getMessage());
        restResponse.setMessage("DataNotFoundException");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restResponse);
    }

    @ExceptionHandler(value = DataExistingException.class)
    public ResponseEntity<RestResponse<Object>> handleDataExistingException(DataExistingException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(BAD_REQUEST.value());
        restResponse.setErrorMessage(e.getMessage());
        restResponse.setMessage("DataExistingException");
        return ResponseEntity.status(BAD_REQUEST).body(restResponse);
    }

}
