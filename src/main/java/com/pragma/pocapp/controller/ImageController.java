package com.pragma.pocapp.controller;

import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api2")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images")
    public List<Image> listAllImages(){
        return imageService.getImages();
    }

    @GetMapping("image")
    public ResponseEntity<Image> getImage(
            @RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") Long idNumber){
        Image image = imageService.searchImage(idType,idNumber);
        return new ResponseEntity<>(image, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Image> registerImage(@Valid @RequestBody Image image) {
        imageService.addImage(image);
        return new ResponseEntity<>(image,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Image> updateImage(@Valid @RequestBody Image image) {
        imageService.updateImage(image);
        return new ResponseEntity<>(image,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("image")
    public ResponseEntity<String> deleteImage(
            @RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") Long idNumber){
        imageService.deleteImage(idType,idNumber);
        return new ResponseEntity<>("User image deleted",HttpStatus.ACCEPTED);
    }


}
