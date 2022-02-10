package com.pragma.pocapp.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientDto implements Serializable {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String idType;
    private final Long idNumber;
    private final Integer age;
    private final String cityOfBirth;
    private final String imageBase64;
}
