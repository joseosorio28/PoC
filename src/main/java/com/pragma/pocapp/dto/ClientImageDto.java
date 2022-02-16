package com.pragma.pocapp.dto;

import lombok.*;

@Data
public class ClientImageDto {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String idType;
    private Long idNumber;
    private Integer age;
    private String cityOfBirth;
    private String id;
    private String imageB64;
}
