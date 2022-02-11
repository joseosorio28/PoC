package com.pragma.pocapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="images")
public class Image {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long id;

    @Column(name = "imageB64")
    private String imageB64;

//    @Column(name = "clientId", nullable = false)
//    private Long clientId;

    @OneToOne//(mappedBy = "image")//(fetch = FetchType.LAZY,  cascade = CascadeType.ALL, mappedBy = "image")
    //@MapsId
    @JoinColumn(name = "client_id")
    //@JoinColumn(name = "clientId", referencedColumnName = "clientId", nullable = false, unique = true)
    private Client  client;

}
