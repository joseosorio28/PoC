package com.pragma.pocapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="clients")
public class Client {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "idType", nullable = false)
    private String idType;

    @Column(name = "idNumber", nullable = false)
    private Long idNumber;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "cityOfBirth")
    private String cityOfBirth;

}