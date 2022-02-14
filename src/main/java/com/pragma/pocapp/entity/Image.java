package com.pragma.pocapp.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name ="images")
public class Image {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long id;

    @NonNull
    @ToString.Include
    @Column(name = "imageB64")
    private String imageB64;

    @NonNull
    @ToString.Include
    @Column(name = "idType", nullable = false)
    private String idType;

    @NonNull
    @ToString.Include
    @Column(name = "idNumber", nullable = false)
    private Long idNumber;

    @OneToOne(mappedBy = "image", fetch = FetchType.LAZY)//(mappedBy = "image")//(fetch = FetchType.LAZY,  cascade = CascadeType.ALL, mappedBy = "image")
    //@MapsId
    //@JoinColumn(name = "client_id")
    //@JoinColumn(name = "clientId", referencedColumnName = "clientId", nullable = false, unique = true)
    private Client  client;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (!imageB64.equals(image.imageB64)) return false;
        if (!idType.equals(image.idType)) return false;
        return idNumber.equals(image.idNumber);
    }

    @Override
    public int hashCode() {
        int result = imageB64.hashCode();
        result = 31 * result + idType.hashCode();
        result = 31 * result + idNumber.hashCode();
        return result;
    }
}
