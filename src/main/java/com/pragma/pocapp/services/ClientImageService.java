package com.pragma.pocapp.services;

import com.pragma.pocapp.entity.ClientImage;
import com.pragma.pocapp.repository.ClientImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientImageService {

    private final ClientImageRepository clientImageRepository;

    @Autowired
    public ClientImageService(ClientImageRepository clientImageRepository) {
        this.clientImageRepository = clientImageRepository;
    }

    public List<ClientImage> getClientsImages() {
        return this.clientImageRepository.findAll();
    }

    public ClientImage searchClientImage(Long id) {
        return clientImageRepository.findById(id)
                .orElseThrow(()->new IllegalStateException("Client image not present in DB"));
    }

    public void addClientImage(ClientImage clientImage) {
        Optional<ClientImage> registeredClient = clientImageRepository.findById(clientImage.getId());

        if (registeredClient.isPresent()) {
            throw new IllegalStateException("Client image already in DB");
        }
        clientImageRepository.save(clientImage);
    }

    public void updateClientImage(ClientImage clientImage) {
        clientImageRepository.save(clientImage);
    }

    public void deleteClientImage(Long id) {
        Optional<ClientImage> registeredClient = clientImageRepository.findById(id);
        registeredClient.ifPresent(clientImage -> clientImageRepository.deleteById(clientImage.getId()));
    }
}
