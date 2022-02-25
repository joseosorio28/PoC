package com.pragma.pocapp.services;

import com.pragma.pocapp.advisor.customexceptions.*;
import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.repository.ClientRepository;
import com.pragma.pocapp.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ImageRepository imageRepository;
    private final ClientMapper clientMapper;

    private static final int MAX_AGE = 100;
    private static final int MIN_AGE = 1;

    //Method for handle single GET request that returns all clients
    public List<ClientImageDto> getClients() {
        List<Client> clients = clientRepository.findAll();
        return clientMapper.toDtos(clients, imagesAvailable(clients));
    }

    //Method for handle single GET request that returns all clients by age
    public List<ClientImageDto> getClientsByAge(Integer age) {
        if (age >= MIN_AGE && age <= MAX_AGE) {
            List<Client> clients = clientRepository.findByAgeGreaterThan(age);
            if (clients.isEmpty()) {
                throw new ClientByAgeNotFoundException(age);
            }
            return clientMapper.toDtos(clients, imagesAvailable(clients)
            );
        } else {
            throw new ClientSearchAgeException();
        }
    }

    public List<Image> imagesAvailable(List<Client> clients) {
        return imageRepository.findAllByIdNumberIn(clients.stream().
                map(Client::getIdNumber).collect(Collectors.toList()));
    }

    //Method for handle single GET request that returns one client
    @Transactional
    public ClientImageDto searchClient(String idType, Long idNumber) {
        return clientMapper.toDto(
                clientRepository
                        .findFirstByIdTypeAndIdNumber(idType, idNumber)
                        .orElseThrow(() -> new ClientNotFoundException(idType, idNumber)),
                imageRepository
                        .findFirstByIdTypeAndIdNumber(idType, idNumber)
                        .orElseGet(Image::new));
    }

    //Method for handle single POST request
    @Transactional
    public void addClient(ClientImageDto newClientDto) {
        Client newClient = clientMapper.toClient(newClientDto);
        clientRepository
                .findFirstByIdTypeAndIdNumber(
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

        updateImage(clientMapper.toImage(newClientDto),
                newClient.getIdType(),
                newClient.getIdNumber());
    }

    //Method for handle single PUT request by client IdType and IdNumber

    public Optional<Client> searchInClientList(List<Client> clients, String idType, Long idNumber) {
        return clients
                .stream()
                .filter(clientSearch ->
                        clientSearch.getIdType().equals(idType) &&
                                clientSearch.getIdNumber().equals(idNumber))
                .findFirst();
    }

    @Transactional
    public void updateClientReview(ClientImageDto clientDto, String idTypeRequest, Long idNumberRequest) {
        String idTypeInJson = clientDto.getIdType();
        Long idNumberInJson = clientDto.getIdNumber();
        List<Client> clients = clientRepository.findAllByIdNumberIn(Arrays.asList(idNumberRequest, idNumberInJson));

        Optional<Client> clientFoundByRequest = searchInClientList(clients, idTypeRequest, idNumberRequest);
        boolean isClientPresentByRequestParam = clientFoundByRequest.isPresent();
        Optional<Client> clientFoundByJson = searchInClientList(clients, idTypeInJson, idNumberInJson);
        boolean isClientPresentByJsonData = clientFoundByJson.isPresent();

        if (!isClientPresentByJsonData && isClientPresentByRequestParam) {//Client present in DB so update it
            updateClientAndImage(clientDto, clientFoundByRequest.get(), idTypeRequest, idNumberRequest);
        } else if (!isClientPresentByJsonData) {
            clientRepository.save(clientMapper.toClient(clientDto));//Client json/request not present then create new client
            updateImage(clientMapper.toImage(clientDto), idTypeRequest, idNumberRequest);
        } else if (isClientPresentByRequestParam) {
            if ((idTypeInJson + idNumberInJson).equals(idTypeRequest + idNumberRequest)) {//Client present in DB so update it
                updateClientAndImage(clientDto, clientFoundByRequest.get(), idTypeRequest, idNumberRequest);
            } else {
                throw new ClientUpdateException(idTypeRequest, idNumberRequest,
                        idTypeInJson, idNumberInJson);//Client present but update info already in DB
            }
        } else {
            throw new ClientNotFoundException(idTypeRequest, idNumberRequest);//Client requested to update not present in DB (json present)
        }
    }

    public void updateClientAndImage(ClientImageDto updatedClient, Client clientFound,
                                     String idType, Long idNumber) {
        updateClient(clientMapper.toClient(updatedClient), clientFound);
        updateImage(clientMapper.toImage(updatedClient), idType, idNumber);
    }

    public void updateClient(Client clientInfo, Client presentClient) {
        presentClient.setFirstName(clientInfo.getFirstName());
        presentClient.setLastName(clientInfo.getLastName());
        presentClient.setIdType(clientInfo.getIdType());
        presentClient.setIdNumber(clientInfo.getIdNumber());
        presentClient.setAge(clientInfo.getAge());
        presentClient.setCityOfBirth(clientInfo.getCityOfBirth());
        clientRepository.save(presentClient);
    }

    public void updateImage(Image image, String idTypeRequest, Long idNumberRequest) {
        imageRepository
                .findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest)
                .ifPresentOrElse(presentImage -> {
                    presentImage.setImageB64(image.getImageB64());
                    presentImage.setIdType(image.getIdType());
                    presentImage.setIdNumber(image.getIdNumber());
                    imageRepository.save(presentImage);
                }, () -> imageRepository.save(image));
    }

    //Method for handle single PUT request by client IdType and IdNumber
    @Transactional
    public void deleteClient(String idType, Long idNumber) {
        clientRepository
                .findFirstByIdTypeAndIdNumber(idType, idNumber)
                .ifPresentOrElse(
                        client -> clientRepository.deleteByClientId(client.getClientId()),
                        () -> {
                            throw new ClientNotFoundException(idType, idNumber);
                        });
        imageRepository
                .findFirstByIdTypeAndIdNumber(idType, idNumber)
                .ifPresent(image -> imageRepository.deleteById(image.getId())
                );
    }

}
