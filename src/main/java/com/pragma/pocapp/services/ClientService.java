package com.pragma.pocapp.services;

import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    //Method for handle single GET request that returns all clients
    public List<ClientImageDto> getClients() {
        return clientMapper.toDtos(clientRepository.findAll());
    }

    //Method for handle single GET request that returns one client
    public ClientImageDto searchClient(String idType, Long idNumber) {
        return clientMapper.toDto(clientRepository
                .findByIdTypeAndIdNumber(idType, idNumber)
                .orElseThrow(() -> new IllegalStateException("Client not present in DB")));
    }

    //Method for handle single POST request
    public void addClient(ClientImageDto newClientDto) {
        Client newClient = clientMapper.toEntity(newClientDto);
        clientRepository
                .findByIdTypeAndIdNumber(
                        newClient.getIdType(),
                        newClient.getIdNumber())
                .ifPresentOrElse(
                        client -> {
                            throw new IllegalStateException("Client already in DB");
                        },
                        () ->
                                clientRepository.save(newClient));
    }

    //Method for handle single PUT request by client IdType and IdNumber
    @Transactional
    public ClientImageDto updateClient(ClientImageDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        return clientMapper.toDto(
                clientRepository
                        .findByIdTypeAndIdNumber(client.getIdType(), client.getIdNumber())
                        .map(presentClient -> {
                            presentClient.setFirstName(client.getFirstName());
                            presentClient.setLastName(client.getLastName());
                            presentClient.setIdType(client.getIdType());
                            presentClient.setIdNumber(client.getIdNumber());
                            presentClient.setAge(client.getAge());
                            presentClient.setCityOfBirth(client.getCityOfBirth());
                            return clientRepository.save(presentClient);
                        })
                        .orElseGet(() ->
                                clientRepository.save(client)));
    }

    //Method for handle single PUT request by client IdType and IdNumber
    public void deleteClient(String idType, Long idNumber) {
        clientRepository
                .findByIdTypeAndIdNumber(idType, idNumber)
                .ifPresentOrElse(
                        client ->
                                clientRepository.deleteById(client.getClientId())
                        ,
                        () -> {
                            throw new IllegalStateException("Client not present in DB");
                        });
    }
}
