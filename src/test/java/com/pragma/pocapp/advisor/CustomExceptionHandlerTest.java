package com.pragma.pocapp.advisor;

import com.pragma.pocapp.entity.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {

    CustomExceptionHandler customExceptionHandler;
    private static Client client;
    private static final String FATAL_ERROR_CONTACT_ADMIN = "The application got a critical error, please contact the administrator.";

    @BeforeAll
    static void beforeAll() {
        //Given:
        client = new Client(1L, "firstname1", "lastname1",
                "CC1", 1L, 30, "city1");
    }

    @BeforeEach
    void setUp() {
        customExceptionHandler = new CustomExceptionHandler();
    }

    @Test
    void canHandleAllExceptions() {
        //Given
        String idType = "CC";
        Long idNumber = 1234L;
        Integer age = 10;
        List<Exception> exceptions = new ArrayList<>();
        exceptions.add(new DefaultException(FATAL_ERROR_CONTACT_ADMIN));
        exceptions.add(new ClientFoundException(idType, idNumber));
        exceptions.add(new ClientNotFoundException(idType, idNumber));
        exceptions.add(new ClientByAgeNotFoundException(age));
        exceptions.add(new ClientUpdateException(idType, idNumber, "TI", 4321L));
        exceptions.add(new ClientSearchAgeException());

        //When
        List<ResponseEntity<ErrorResponse>> responses = new ArrayList<>();
        responses.add(customExceptionHandler.handleAllExceptions(exceptions.get(0)));
        responses.add(customExceptionHandler.handleAllExceptions(exceptions.get(1)));
        responses.add(customExceptionHandler.handleAllExceptions(exceptions.get(2)));
        responses.add(customExceptionHandler.handleAllExceptions(exceptions.get(3)));
        responses.add(customExceptionHandler.handleAllExceptions(exceptions.get(4)));
        responses.add(customExceptionHandler.handleAllExceptions(exceptions.get(5)));

        //Then
        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responses.get(0).getStatusCodeValue()),
                () -> assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), responses.get(1).getStatusCodeValue()),
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), responses.get(2).getStatusCodeValue()),
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), responses.get(3).getStatusCodeValue()),
                () -> assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), responses.get(4).getStatusCodeValue()),
                () -> assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), responses.get(5).getStatusCodeValue())

        );
    }

    @Test
    void cantFindCustomExceptionClass() {
        //Given
        Exception exception = new IllegalAccessException();

        //When
        ResponseEntity<ErrorResponse> response = customExceptionHandler.handleAllExceptions(exception);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void canHandleMethodArgumentNotValid() {
        //Given
        BindingResult result = new BeanPropertyBindingResult(client, "client");
        result.rejectValue("clientId", "#FF", "Error");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, result);

        //When
        ResponseEntity<Object> response = customExceptionHandler.handleMethodArgumentNotValid(
                exception,
                HttpHeaders.EMPTY,
                HttpStatus.UNPROCESSABLE_ENTITY,
                null);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCodeValue());
    }

    @Test
    void errorResponseModel() {
        //Given
        ErrorResponse errorResponse = new ErrorResponse(
                Exception.class.getSimpleName(),
                "",
                HttpStatus.OK
        );

        //When
        errorResponse.setTimestamp(new Date(1645441200000L));
        errorResponse.setExceptionName(DefaultException.class.getSimpleName());
        errorResponse.setMessage(FATAL_ERROR_CONTACT_ADMIN);
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());

        //Then
        assertAll(
                () -> assertEquals(new Date(1645441200000L), errorResponse.getTimestamp()),
                () -> assertEquals(DefaultException.class.getSimpleName(), errorResponse.getExceptionName()),
                () -> assertEquals(FATAL_ERROR_CONTACT_ADMIN, errorResponse.getMessage()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), errorResponse.getStatus())
        );
    }

}