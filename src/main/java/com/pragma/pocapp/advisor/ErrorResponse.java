package com.pragma.pocapp.advisor;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private String exceptionName;
    private String message;
    private int code;
    private String status;

    public ErrorResponse(String exceptionName, String message, HttpStatus httpStatus) {
        this.timestamp = new Date();
        this.exceptionName = exceptionName;
        this.message = message;
        this.code = httpStatus.value();
        this.status = httpStatus.name();
    }

}
