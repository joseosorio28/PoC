package com.pragma.pocapp.advisor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String FATAL_ERROR_CONTACT_ADMIN = "The application got a critical error, please contact the administrator.";
    private static final ConcurrentHashMap<String, Integer> STATUS_CODES = new ConcurrentHashMap<>();

    public CustomExceptionHandler() {
        STATUS_CODES.put(DefaultException.class.getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        STATUS_CODES.put(ClientFoundException.class.getSimpleName(), HttpStatus.NOT_ACCEPTABLE.value());
        STATUS_CODES.put(ClientNotFoundException.class.getSimpleName(), HttpStatus.NOT_FOUND.value());
        STATUS_CODES.put(ClientByAgeNotFoundException.class.getSimpleName(), HttpStatus.NOT_FOUND.value());
        STATUS_CODES.put(ClientUpdateException.class.getSimpleName(), HttpStatus.NOT_ACCEPTABLE.value());
        STATUS_CODES.put(ClientSearchAgeException.class.getSimpleName(), HttpStatus.NOT_ACCEPTABLE.value());
        STATUS_CODES.put(DataIntegrityViolationException.class.getSimpleName(), HttpStatus.NOT_ACCEPTABLE.value());
        STATUS_CODES.put(ConstraintViolationException.class.getSimpleName(), HttpStatus.NOT_ACCEPTABLE.value());
    }

    @ExceptionHandler(value
            = {Exception.class})
    protected ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception) {

        ResponseEntity<ErrorResponse> response;
        String exceptionName = exception.getClass().getSimpleName();
        String message = exception.getMessage();
        Integer code = STATUS_CODES.get(exceptionName);

        if (code != null) {
            ErrorResponse errorResponse = new ErrorResponse(exceptionName, message, HttpStatus.valueOf(code));
            response = new ResponseEntity<>(errorResponse, HttpStatus.valueOf(code));
        } else {
            ErrorResponse errorResponse = new ErrorResponse(exceptionName, FATAL_ERROR_CONTACT_ADMIN, HttpStatus.INTERNAL_SERVER_ERROR);
            response = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessage
                    .append("Please verify field : ")
                    .append(fieldError.getField().toUpperCase())
                    .append(" -> ")
                    .append(fieldError.getDefaultMessage())
                    .append(". ");
        }
        ErrorResponse errorResponse = new ErrorResponse(ex.getClass().getSimpleName(),
                "Validation errors. " + errorMessage,
                status);
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }
}
