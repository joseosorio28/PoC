package com.pragma.pocapp.repository;

import com.pragma.pocapp.entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {

    Optional<Image> findImageByIdTypeAndIdNumber(String idType, Long idNumber);

    List<Image> findAll();

    void deleteImageByIdTypeAndIdNumber(String idType, Long idNumber);
}