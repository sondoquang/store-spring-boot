package com.stlang.store.exception;

import com.stlang.store.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(BAD_REQUEST.value());
        restResponse.setErrorMessage(e.getMessage());
        restResponse.setMessage("IdInvalidException");
        return ResponseEntity.status(BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RestResponse<Object>> handleAllException(Exception e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(INTERNAL_SERVER_ERROR.value());
        restResponse.setErrorMessage(e.getMessage());
        restResponse.setMessage("Internal Server Error");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(restResponse);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<RestResponse<Object>> handleException(Exception e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(BAD_REQUEST.value());
        restResponse.setErrorMessage(e.getMessage());
        restResponse.setMessage("Bad Credentials");
        return ResponseEntity.status(BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = CustomFileUploadException.class)
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(CustomFileUploadException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(BAD_REQUEST.value());
        restResponse.setErrorMessage(e.getMessage());
        restResponse.setMessage("Error uploading file");
        return ResponseEntity.status(BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = DataIncorrectFormatException.class)
    public ResponseEntity<RestResponse<Object>> handleDataIncorrectFormatException(DataIncorrectFormatException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(BAD_REQUEST.value());
        restResponse.setErrorMessage(e.getMessage());
        restResponse.setMessage("Error data incorrect format");
        return ResponseEntity.status(BAD_REQUEST).body(restResponse);
    }

}
