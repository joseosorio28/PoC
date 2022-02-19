package com.pragma.pocapp.services;

import com.pragma.pocapp.advisor.*;
import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.repository.ClientRepository;
import com.pragma.pocapp.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ImageRepository imageRepository;
    private final ClientMapper clientMapper;

    //Method for handle single GET request that returns all clients
    public List<ClientImageDto> getClients() {
        return clientMapper.toDtos(
                Optional.of(clientRepository.findAll())
                        .orElseThrow(ClientNotFoundException::new),
                imageRepository
                        .findAll());
    }

    //Method for handle single GET request that returns all clients by age
    public List<ClientImageDto> getClientsByAge(Integer age) {
        return clientMapper.toDtos(
                clientRepository
                        .findByAgeGreaterThan(age)
                        .orElseThrow(() -> new ClientByAgeNotFoundException(age)),
                imageRepository
                        .findAll());
    }

    //Method for handle single GET request that returns one client
    @Transactional
    public ClientImageDto searchClient(String idType, Long idNumber) {
        return clientMapper.toDto(
                clientRepository
                        .findByIdTypeAndIdNumber(idType, idNumber)
                        .orElseThrow(() -> new ClientNotFoundException(idType, idNumber)),
                imageRepository
                        .findByIdTypeAndIdNumber(idType, idNumber)
                        .orElseGet(Image::new));
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
                            throw new ClientFoundException(
                                    client.getIdType(),
                                    client.getIdNumber());
                        },
                        () ->
                                clientRepository.save(newClient));

        Image newClientImage = clientMapper.toImage(newClientDto);
        imageRepository.save(
                imageRepository
                        .findByIdTypeAndIdNumber(
                                newClientImage.getIdType(),
                                newClientImage.getIdNumber())
                        .orElse(newClientImage));
    }

    //Method for handle single PUT request by client IdType and IdNumber
    @Transactional
    public void updateClientReview(ClientImageDto clientDto, String idTypeRequest, Long idNumberRequest) {
        Client client = clientMapper.toClient(clientDto);
        Image image = clientMapper.toImage(clientDto);
        String idTypeInJson = client.getIdType();
        Long idNumberInJson = client.getIdNumber();

        List<Client> clients = clientRepository.findAll();

        Optional<Client> clientFoundByRequest = clients
                .stream()
                .filter(clientSearch ->
                        clientSearch.getIdType().equals(idTypeRequest) &&
                                clientSearch.getIdNumber().equals(idNumberRequest))
                .findFirst();
        boolean isClientPresentByRequestParam = clientFoundByRequest.isPresent();

        Optional<Client> clientFoundByJson = clients
                .stream()
                .filter(clientSearch ->
                        clientSearch.getIdType().equals(idTypeInJson) &&
                                clientSearch.getIdNumber().equals(idNumberInJson))
                .findFirst();
        boolean isClientPresentByJsonData = clientFoundByJson.isPresent();

        if (!isClientPresentByJsonData && isClientPresentByRequestParam) {
            clientFoundByRequest.ifPresent(
                    presentClient->{
                        updateClient(presentClient,client);
                        updateImage(image, idTypeRequest, idNumberRequest);});
        } else if (!isClientPresentByJsonData) {
            clientRepository.save(client);//Client not present then create new client
            updateImage(image, idTypeRequest, idNumberRequest);
        } else if (isClientPresentByRequestParam) {
            if ((idTypeInJson.equals(idTypeRequest)) &&
                    (idNumberInJson.equals(idNumberRequest))) {
                clientFoundByRequest.ifPresent(
                        presentClient->{
                            updateClient(presentClient,client);
                            updateImage(image, idTypeRequest, idNumberRequest);});
            } else {
                throw new ClientUpdateException(idTypeRequest, idNumberRequest,
                        idTypeInJson, idNumberInJson);//Client already in DB with another data
            }
        } else {
            throw new ClientNotFoundException(idTypeRequest, idNumberRequest);//Client requested to update not present in DB
        }
    }

    public void updateClient(Client presentClient, Client client) {
            presentClient.setFirstName(client.getFirstName());
            presentClient.setLastName(client.getLastName());
            presentClient.setIdType(client.getIdType());
            presentClient.setIdNumber(client.getIdNumber());
            presentClient.setAge(client.getAge());
            presentClient.setCityOfBirth(client.getCityOfBirth());
            clientRepository.save(presentClient);
    }

    public void updateImage(Image image, String idTypeRequest, Long idNumberRequest) {
        imageRepository
                .findByIdTypeAndIdNumber(idTypeRequest, idNumberRequest)
                .ifPresentOrElse(presentImage -> {
                    presentImage.setImageB64(image.getImageB64());
                    presentImage.setIdType(image.getIdType());
                    presentImage.setIdNumber(image.getIdNumber());
                    imageRepository.save(presentImage);
                }, () -> {
                    Image newImage = new Image();
                    newImage.setImageB64(image.getImageB64());
                    newImage.setIdType(image.getIdType());
                    newImage.setIdNumber(image.getIdNumber());
                    imageRepository.save(newImage);
                });
    }

    //Method for handle single PUT request by client IdType and IdNumber
    @Transactional
    public void deleteClient(String idType, Long idNumber) {
        clientRepository
                .findByIdTypeAndIdNumber(idType, idNumber)
                .ifPresentOrElse(
                        client ->
                                clientRepository.deleteByClientId(client.getClientId())
                        ,
                        () -> {
                            throw new ClientNotFoundException(idType, idNumber);
                        });
        imageRepository
                .findByIdTypeAndIdNumber(idType, idNumber)
                .ifPresentOrElse(
                        image ->
                                imageRepository.deleteById(image.getId())
                        ,
                        () -> {
                            throw new ClientNotFoundException(idType, idNumber);
                        });
    }

}
