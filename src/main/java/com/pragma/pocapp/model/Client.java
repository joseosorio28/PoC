package com.pragma.pocapp.model;

import javax.persistence.*;

@Entity
@Table
public class Client {

    @Id
    @Column(name = "IdKey", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "IdType", nullable = false)
    private String idType;

    @Column(name = "IdNumber", nullable = false)
    private Long idNumber;

    @Column(name = "Age", nullable = false)
    private Integer age;

    @Column(name = "CityOfBirth")
    private String cityOfBirth;

    public Client() {
    }

    public Client(Long id, String firstName, String lastName,
                  String idType, Long idNumber, Integer age, String cityOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idType = idType;
        this.idNumber = idNumber;
        this.age = age;
        this.cityOfBirth = cityOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public Long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Long idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCityOfBirth() {
        return cityOfBirth;
    }

    public void setCityOfBirth(String cityOfBirth) {
        this.cityOfBirth = cityOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (!id.equals(client.id)) return false;
        if (!firstName.equals(client.firstName)) return false;
        if (!idType.equals(client.idType)) return false;
        return idNumber.equals(client.idNumber);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + idType.hashCode();
        result = 31 * result + idNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", idType='" + idType + '\'' +
                ", idNumber=" + idNumber +
                ", age=" + age +
                ", cityOfBirth='" + cityOfBirth + '\'' +
                '}';
    }
}
