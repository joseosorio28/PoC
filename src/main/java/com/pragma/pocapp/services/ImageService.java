package com.pragma.pocapp.services;

import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getImages() {
        return this.imageRepository.findAll();
    }

    public Image searchImage(String idType,Long idNumber) {
        return imageRepository.findImageByIdTypeAndIdNumber(idType,idNumber)
                .orElseThrow(()->new IllegalStateException("Client image not present in DB"));
    }

    public void addImage(Image image) {
        Optional<Image> imageByClientId = imageRepository.findImageByIdTypeAndIdNumber(image.getIdType(),image.getIdNumber());

        if (imageByClientId.isPresent()) {
            throw new IllegalStateException("Client image already in DB");
        }
        imageRepository.save(image);
    }

    public void updateImage(Image image) {
        imageRepository.save(image);
    }

    public void deleteImage(String idType,Long idNumber) {
        Optional<Image> imageByClientId = imageRepository.findImageByIdTypeAndIdNumber(idType,idNumber);
        imageByClientId.ifPresent(image -> imageRepository.deleteImageByIdTypeAndIdNumber(idType,idNumber));
    }
}
