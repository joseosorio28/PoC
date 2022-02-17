package com.pragma.pocapp.repository;

import com.pragma.pocapp.entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {

    Optional<Image> findByIdTypeAndIdNumber(String idType, Long idNumber);

    List<Image> findAll();

    default Optional<List<Image>> findByIdTypeAndIdNumberIn(List<String> idTypes, List<Long> idNumbers) {
        List<Image> list = new ArrayList<>(idTypes.size());
        int i=0;
        for (String idType : idTypes) {
            list.add(findByIdTypeAndIdNumber(idType,idNumbers.get(i)).orElse(null));
            i++;
        }
        return Optional.of(list);
    }

}