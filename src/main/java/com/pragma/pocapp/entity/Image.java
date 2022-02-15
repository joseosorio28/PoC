package com.pragma.pocapp.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Document(collection = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

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
