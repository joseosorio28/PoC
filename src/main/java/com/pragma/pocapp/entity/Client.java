package com.pragma.pocapp.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @Column(name = "clientId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long clientId;

    @ToString.Include
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @ToString.Include
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @ToString.Include
    @Column(name = "idType", nullable = false)
    private String idType;

    @ToString.Include
    @Column(name = "idNumber", nullable = false)
    private Long idNumber;

    @ToString.Include
    @Column(name = "age", nullable = false)
    private Integer age;

    @ToString.Include
    @Column(name = "cityOfBirth", nullable = false)
    private String cityOfBirth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (!idType.equals(client.idType)) return false;
        return idNumber.equals(client.idNumber);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + idType.hashCode();
        result = 31 * result + idNumber.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + cityOfBirth.hashCode();
        return result;
    }
}