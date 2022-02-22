package com.pragma.pocapp.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ClientImageDto {

    private Long clientId;

    @NotEmpty(message = "Field required to create/update client")
    @Size(min = 2, max = 50, message = "The length of name must be between 2 and 50 characters.")
    private String firstName;

    @NotEmpty(message = "Field required to create/update client")
    @Size(min = 2, max = 100, message = "The length of lastname name must be between 2 and 50 characters.")
    private String lastName;

    @NotEmpty(message = "Field required to create/read/update/delete client")
    @Size(min = 2, max = 5, message = "CC/TI/PP, the length of full name must be between 2 and 5 characters.")
    private String idType;

    @NotNull(message = "Field required to create/update client")
    @Range(min =1, message = "Field required to create/read/update/delete client. " +
            "Must be positive number representing identification")
    private Long idNumber;

    @NotNull(message = "Field required to create/update client")
    @Range(min =1, max = 99, message = "Age must be between 1 and 99")
    private Integer age;

    @NotEmpty(message = "Field required to create/update client")
    @Size(min = 2, max = 100, message = "The length of the city must be between 2 and 50 characters.")
    private String cityOfBirth;

    private String id;

    @NotEmpty(message = "Field required to create/update client")
    private String imageB64;
}
