package com.pragma.pocapp.services;

import com.pragma.pocapp.advisor.*;
import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.repository.ClientRepository;
import com.pragma.pocapp.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ImageRepository imageRepository;
    private final ClientMapper clientMapper;

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
        return clientMapper.toDtos(Optional.of(
                                clientRepository
                                        .findAll())
                        .orElseThrow(ClientNotFoundException::new),
                imageRepository
                        .findAll());
    }

    //Method for handle single GET request that returns all clients by age
    public List<ClientImageDto> getClientsByAge(Integer age) {
        List<Client> clients = clientRepository
                .findByAgeGreaterThan(age)
                .orElseThrow(() -> new ClientByAgeNotFoundException(age));
        List<Image> images = imageRepository
                .findByIdTypeAndIdNumberIn(
                        clients.stream().map(Client::getIdType).collect(Collectors.toList()),
                        clients.stream().map(Client::getIdNumber).collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
        return clientMapper.toDtos(clients, images);
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
                                    newClient.getIdType(),
                                    newClient.getIdNumber());
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
    public ClientImageDto updateClient(ClientImageDto clientDto, String idType, Long idNumber) {
        Client client = clientMapper.toClient(clientDto);
        Image image = clientMapper.toImage(clientDto);

        int updateCase = selectUpdateCase(idType, idNumber, client.getIdType(), client.getIdNumber());

        switch (updateCase) {
            case 1://Update fields
                return clientMapper.toDto(
                        clientRepository
                                .findByIdTypeAndIdNumber(idType, idNumber)
                                .map(presentClient -> {
                                    presentClient.setFirstName(client.getFirstName());
                                    presentClient.setLastName(client.getLastName());
                                    presentClient.setIdType(client.getIdType());
                                    presentClient.setIdNumber(client.getIdNumber());
                                    presentClient.setAge(client.getAge());
                                    presentClient.setCityOfBirth(client.getCityOfBirth());
                                    return clientRepository.save(presentClient);
                                }).orElseThrow(DefaultException::new),
                        imageRepository
                                .findByIdTypeAndIdNumber(idType, idNumber)
                                .map(presentImage -> {
                                    presentImage.setImageB64(image.getImageB64());
                                    presentImage.setIdType(image.getIdType());
                                    presentImage.setIdNumber(image.getIdNumber());
                                    return imageRepository.save(presentImage);
                                }).orElseGet(() -> {
                                    Image newImage = new Image();
                                    newImage.setImageB64(image.getImageB64());
                                    newImage.setIdType(image.getIdType());
                                    newImage.setIdNumber(image.getIdNumber());
                                    return imageRepository.save(newImage);
                                }));
            case 2://Create new client
                return clientMapper.toDto(
                        clientRepository.save(client),
                        imageRepository.save(image));
            case 3://Client already in DB with another data
                throw new ClientUpdateException(idType, idNumber, client.getIdType(), client.getIdNumber());
            case 4://Client requested to update not present in DB
                throw new ClientNotFoundException(idType, idNumber);
            default://Something awful happened
                throw new DefaultException();
        }
    }

    private int selectUpdateCase(String idTypeRequest, Long idNumberRequest,
                                 String idTypeInJson, Long idNumberInJson) {

        boolean isClientPresentByRequestParam = clientRepository
                .findByIdTypeAndIdNumber(idTypeRequest, idNumberRequest)
                .isPresent();
        boolean isClientPresentByJsonData = clientRepository
                .findByIdTypeAndIdNumber(idTypeInJson, idNumberInJson)
                .isPresent();

        if (!isClientPresentByJsonData && isClientPresentByRequestParam) {
            return 1;//Client present then update fields
        } else if (!isClientPresentByJsonData) {
            return 2;//Client not present then create new client
        } else if (isClientPresentByRequestParam) {
            if ((Objects.equals(idTypeInJson, idTypeRequest)) &&
                    (Objects.equals(idNumberInJson, idNumberRequest))) {
                return 1;//Client present then update fields
            } else {
                return 3;//Client already in DB with another data
            }
        } else {
            return 4;//Client requested to update not present in DB
        }
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
