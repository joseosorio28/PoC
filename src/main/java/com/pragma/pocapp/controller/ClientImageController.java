package com.pragma.pocapp.controller;

import com.pragma.pocapp.entity.ClientImage;
import com.pragma.pocapp.services.ClientImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api2")
public class ClientImageController {

    private final ClientImageService clientImageService;

    @Autowired
    public ClientImageController(ClientImageService clientImageService) {
        this.clientImageService = clientImageService;
    }

    @GetMapping("/clientsImages")
    public List<ClientImage> listAllClientsImages(){
        return clientImageService.getClientsImages();
    }

    @GetMapping("clientImage")
    public ResponseEntity<ClientImage> getClientImage(
            @RequestParam(name = "id") Long id){
        ClientImage clientImage = clientImageService.searchClientImage(id);
        return new ResponseEntity<>(clientImage, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<ClientImage> registerClientImage(@Valid @RequestBody ClientImage clientImage) {
        clientImageService.addClientImage(clientImage);
        return new ResponseEntity<>(clientImage,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ClientImage> updateClientImage(@Valid @RequestBody ClientImage clientImage) {
        clientImageService.updateClientImage(clientImage);
        return new ResponseEntity<>(clientImage,HttpStatus.CREATED);
    }

    @DeleteMapping("clientImage")
    public ResponseEntity<String> deleteClientImage(
            @RequestParam(name = "id") Long id){
        clientImageService.deleteClientImage(id);
        return new ResponseEntity<>("User image deleted",HttpStatus.ACCEPTED);
    }


}
