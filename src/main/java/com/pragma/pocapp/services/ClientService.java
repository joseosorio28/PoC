package com.pragma.pocapp.services;

import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.repository.ClientRepository;
import com.pragma.pocapp.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ImageRepository imageRepository;
    private final ClientMapper clientMapper;
    private static final String NO_CLIENT_FOUND = "Client not present in DB";
    private static final String CLIENT_FOUND = "Client already in DB";


    @Autowired
    public ClientService(ClientRepository clientRepository,
                         ClientMapper clientMapper,
                         ImageRepository imageRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.imageRepository = imageRepository;
    }

    //Method for handle single GET request that returns all clients
    public List<ClientImageDto> getClients() {
        return clientMapper.toDtos(clientRepository.findAll(), imageRepository.findAll());
    }

    //Method for handle single GET request that returns one client
    public ClientImageDto searchClient(String idType, Long idNumber) {
        return clientMapper.toDto(
                clientRepository
                        .findByIdTypeAndIdNumber(idType, idNumber)
                        .orElseThrow(() -> new IllegalStateException(NO_CLIENT_FOUND)),
                imageRepository
                        .findByIdTypeAndIdNumber(idType, idNumber)
                        .orElseThrow(() -> new IllegalStateException(NO_CLIENT_FOUND)));
    }

    //Method for handle single POST request
    @Transactional
    public void addClient(ClientImageDto newClientDto) {
        Client newClient = clientMapper.toClient(newClientDto);
        clientRepository
                .findByIdTypeAndIdNumber(
                        newClient.getIdType(),
                        newClient.getIdNumber())
                .ifPresentOrElse(
                        client -> {
                            throw new IllegalStateException(CLIENT_FOUND);
                        },
                        () ->
                                clientRepository.save(newClient));
        Image newClientImage = clientMapper.toImage(newClientDto);
        imageRepository
                .findByIdTypeAndIdNumber(
                        newClientImage.getIdType(),
                        newClientImage.getIdNumber())
                .ifPresentOrElse(
                        client -> {
                            throw new IllegalStateException(CLIENT_FOUND);
                        },
                        () ->
                                imageRepository.save(newClientImage));
    }

    //Method for handle single PUT request by client IdType and IdNumber
    @Transactional
    public ClientImageDto updateClient(ClientImageDto clientDto) {
        Client client = clientMapper.toClient(clientDto);
        Image image = clientMapper.toImage(clientDto);
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
                                clientRepository.save(client)),
                imageRepository
                        .findByIdTypeAndIdNumber(image.getIdType(), image.getIdNumber())
                        .map(presentImage -> {
                            presentImage.setImageB64(image.getImageB64());
                            presentImage.setIdType(image.getIdType());
                            presentImage.setIdNumber(image.getIdNumber());
                            return imageRepository.save(presentImage);
                        })
                        .orElseGet(() ->
                                imageRepository.save(image))
        );
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
                            throw new IllegalStateException(NO_CLIENT_FOUND);
                        });
        imageRepository
                .findByIdTypeAndIdNumber(idType, idNumber)
                .ifPresentOrElse(
                        image ->
                                imageRepository.deleteById(image.getId())
                        ,
                        () -> {
                            throw new IllegalStateException(NO_CLIENT_FOUND);
                        });
    }
}
