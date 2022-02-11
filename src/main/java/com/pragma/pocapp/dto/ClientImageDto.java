package com.pragma.pocapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientImageDto {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String idType;
    private Long idNumber;
    private Integer age;
    private String cityOfBirth;
    private String imageB64;
}
