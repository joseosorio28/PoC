package com.pragma.pocapp.services;

import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getClients() {
        return this.clientRepository.findAll();
    }

    public Client searchClient(String idType, Long idNumber) {
        return clientRepository.findByIdTypeAndIdNumber(idType,idNumber)
                .orElseThrow(()->new IllegalStateException("Client not present in DB"));
    }

    public void addClient(Client client) {
        Optional<Client> registeredClient = clientRepository.findByIdTypeAndIdNumber(client.getIdType(),client.getIdNumber());

        if (registeredClient.isPresent()) {
            throw new IllegalStateException("Client already in DB");
        }

        clientRepository.save(client);
    }


    public void updateClient(Client client) {
        clientRepository.save(client);
    }

    public void deleteClient(String idType, Long idNumber) {
        Optional<Client> registeredClient = clientRepository.findByIdTypeAndIdNumber(idType,idNumber);
        registeredClient.ifPresent(client -> clientRepository.deleteById(client.getClientId()));
    }
}
