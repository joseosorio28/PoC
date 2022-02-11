package com.pragma.pocapp.entity;

import com.pragma.pocapp.dto.ClientImageDto;
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
    @Column(name = "clientId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long clientId;

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

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "client")
    //@PrimaryKeyJoinColumn
    private Image image;

}