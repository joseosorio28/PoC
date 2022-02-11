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

    public Image searchImage(Long clientId) {
        return imageRepository.findById(clientId)
                .orElseThrow(()->new IllegalStateException("Client image not present in DB"));
    }

    public void addImage(Image image) {
        Optional<Image> imageByClientId = imageRepository.findByClientId(image.getClientId());

        if (imageByClientId.isPresent()) {
            throw new IllegalStateException("Client image already in DB");
        }
        imageRepository.save(image);
    }

    public void updateImage(Image image) {
        imageRepository.save(image);
    }

    public void deleteImage(Long clientId) {
        Optional<Image> imageByClientId = imageRepository.findByClientId(clientId);
        imageByClientId.ifPresent(image -> imageRepository.deleteByClientId(image.getClientId()));
    }
}
