package com.pragma.pocapp.services;

import com.pragma.pocapp.advisor.customexceptions.*;
import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.repository.ClientRepository;
import com.pragma.pocapp.repository.ImageRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ImageRepository imageRepository;

    private final ClientMapper clientMapper = ClientMapper.INSTANCE;

    private ClientService clientService;

    private static Client client, client2;
    private static Image image;

    @BeforeAll
    static void beforeAll() {
        //Given:
        client = new Client(1L, "firstname1", "lastname1",
                "CC", 1L, 30, "city1");

        image = new Image("1", "qwerty1", client.getIdType(), client.getIdNumber());

        client2 = new Client(2L, "firstname2", "lastname2",
                "CC", 2L, 32, "city2");

        Image image2 = new Image("2", "qwerty2", client2.getIdType(), client2.getIdNumber());

    }

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository, imageRepository, clientMapper);
    }

    @Test
    void canGetClients() {
        //Then
        clientService.getClients();

        //When
        verify(clientRepository).findAll();
        verify(imageRepository).findAll();

    }

    @Test
    void canGetClientsByAge() {
        //Given
        int age = 10;
        List<Client> clients = new ArrayList<>(Arrays.asList(client, client2));

        //When
        when(clientRepository.findByAgeGreaterThan(age)).thenReturn(Optional.of(clients));
        clientService.getClientsByAge(age);

        //Then
        verify(clientRepository).findByAgeGreaterThan(age);
        verify(imageRepository).findAll();

    }

    @Test
    void wontGetClientsByAgeCase1() {
        //Given
        int age = 10;

        //When
        when(clientRepository.findByAgeGreaterThan(age)).thenReturn(Optional.empty());//No clients in DB

        //Then
        assertThrows(ClientByAgeNotFoundException.class,
                () -> clientService.getClientsByAge(age));
    }

    @Test
    void wontGetClientsByAgeCase2() {
        //Given
        int age1 = 150;//Age out off bounds
        int age2 = 0;//Age out off bounds

        //When and Then

        assertAll(
                () -> assertThrows(ClientSearchAgeException.class, () -> clientService.getClientsByAge(age1)),
                () -> assertThrows(ClientSearchAgeException.class, () -> clientService.getClientsByAge(age2))
        );
    }

    @Test
    void canSearchClient() {
        //Given
        String idType = client.getIdType();
        Long idNumber = client.getIdNumber();

        //When
        when(clientRepository.findFirstByIdTypeAndIdNumber(idType, idNumber)).thenReturn(Optional.of(client));
        clientService.searchClient(idType, idNumber);

        //Then
        verify(clientRepository).findFirstByIdTypeAndIdNumber(idType, idNumber);
        verify(imageRepository).findFirstByIdTypeAndIdNumber(idType, idNumber);
    }

    @Test
    void wontSearchClient() {
        //Given
        String idType = client.getIdType();
        Long idNumber = client.getIdNumber();
        Optional<Client> clientNotFound = Optional.empty();

        //When
        when(clientRepository.findFirstByIdTypeAndIdNumber(idType, idNumber)).thenReturn(clientNotFound);

        //Then
        assertThrows(ClientNotFoundException.class,
                () -> clientService.searchClient(idType, idNumber));
    }

    @Test
    void canAddClient() {
        //Given
        ClientImageDto clientImageDto = clientMapper.toDto(client, image);
        String idType = client.getIdType();
        Long idNumber = client.getIdNumber();

        //When
        when(clientRepository.findFirstByIdTypeAndIdNumber(idType, idNumber)).thenReturn(Optional.empty());
        when(imageRepository.findFirstByIdTypeAndIdNumber(idType, idNumber)).thenReturn(Optional.of(image));
        clientService.addClient(clientImageDto);

        //Then
        verify(clientRepository).findFirstByIdTypeAndIdNumber(idType, idNumber);
        verify(clientRepository).save(createNewClientWithSameData(client));

        verify(imageRepository).findFirstByIdTypeAndIdNumber(idType, idNumber);
        verify(imageRepository).save(createNewImageWithSameData(image));
    }

    @Test
    void wontAddClient() {
        //Given
        ClientImageDto clientImageDto = clientMapper.toDto(client, image);
        String idType = client.getIdType();
        Long idNumber = client.getIdNumber();

        //When
        when(clientRepository.findFirstByIdTypeAndIdNumber(idType, idNumber)).thenReturn(Optional.of(client));

        //Then
        assertThrows(ClientFoundException.class,
                () -> clientService.addClient(clientImageDto));
    }

    @Test
    void canUpdateClientCase1() {
        //Given
        List<Client> clients = new ArrayList<>(Arrays.asList(client, client2));
        ClientImageDto clientImageDto = clientMapper.toDto(client, image);
        String idTypeRequest = "CC";
        Long idNumberRequest = 1L;

        //When
        when(clientRepository.findAll()).thenReturn(clients);
        when(imageRepository.findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest)).thenReturn(Optional.of(image));
        clientService.updateClientReview(clientImageDto, idTypeRequest, idNumberRequest);

        //Then
        verify(clientRepository).findAll();
        verify(clientRepository).save(createNewClientWithSameData(client));

        verify(imageRepository).findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest);
        verify(imageRepository).save(createNewImageWithSameData(image));
    }

    @Test
    void canUpdateClientCase2() {
        //Given
        Client clientByJson = createNewClientWithSameData(client);
        clientByJson.setIdType("TI");//Data to update
        clientByJson.setIdNumber(3L);

        Image imageByJson = createNewImageWithSameData(image);
        imageByJson.setIdType("TI");//Data to update
        imageByJson.setIdNumber(3L);

        List<Client> clients = new ArrayList<>(Arrays.asList(
                createNewClientWithSameData(client),
                createNewClientWithSameData(client2)));
        ClientImageDto clientImageDto = clientMapper.toDto(clientByJson, imageByJson);
        String idTypeRequest = "CC";
        Long idNumberRequest = 1L;

        //When
        when(clientRepository.findAll()).thenReturn(clients);
        when(imageRepository.findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest))
                .thenReturn(Optional.of(createNewImageWithSameData(image)));
        clientService.updateClientReview(clientImageDto, idTypeRequest, idNumberRequest);

        //Then
        verify(clientRepository).findAll();
        verify(clientRepository).save(clientByJson);

        verify(imageRepository).findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest);
        verify(imageRepository).save(imageByJson);
    }

    @Test
    void wontUpdateClientButCreateIt() {
        //Given
        Client clientByJson = createNewClientWithSameData(client);
        clientByJson.setIdType("CC");//New client id
        clientByJson.setIdNumber(3L);

        Image imageByJson = createNewImageWithSameData(image);
        imageByJson.setIdType("CC");//New client id
        imageByJson.setIdNumber(3L);

        List<Client> clients = new ArrayList<>(Arrays.asList(
                createNewClientWithSameData(client),
                createNewClientWithSameData(client2)));
        ClientImageDto clientImageDto = clientMapper.toDto(clientByJson, imageByJson);
        String idTypeRequest = "TI";
        Long idNumberRequest = 4L;

        //When
        when(clientRepository.findAll()).thenReturn(clients);
        when(imageRepository.findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest)).thenReturn(Optional.empty());
        clientService.updateClientReview(clientImageDto, idTypeRequest, idNumberRequest);

        //Then
        verify(clientRepository).findAll();
        verify(clientRepository).save(clientByJson);

        verify(imageRepository).findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest);
        verify(imageRepository).save(imageByJson);

    }

    @Test
    void canUpdateClientAndCreateImage() {
        //Given
        Client clientByJson = createNewClientWithSameData(client);
        Image imageByJson = createNewImageWithSameData(image);

        List<Client> clients = new ArrayList<>(Arrays.asList(
                createNewClientWithSameData(client),
                createNewClientWithSameData(client2)));
        ClientImageDto clientImageDto = clientMapper.toDto(clientByJson, imageByJson);
        String idTypeRequest = "CC";
        Long idNumberRequest = 1L;

        //When
        when(clientRepository.findAll()).thenReturn(clients);
        when(imageRepository.findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest)).thenReturn(Optional.empty());
        clientService.updateClientReview(clientImageDto, idTypeRequest, idNumberRequest);

        //Then
        verify(clientRepository).findAll();
        verify(clientRepository).save(clientByJson);

        verify(imageRepository).findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest);
        verify(imageRepository).save(imageByJson);

    }

    @Test
    void wontUpdateClientCase1() {
        //Given
        List<Client> clients = new ArrayList<>(Arrays.asList(client, client2));
        ClientImageDto clientImageDto = clientMapper.toDto(client, image);
        String idTypeRequest = "TI";
        Long idNumberRequest = 1L;

        //When
        when(clientRepository.findAll()).thenReturn(clients);

        //Then
        assertThrows(ClientNotFoundException.class,
                () -> clientService.updateClientReview(clientImageDto, idTypeRequest, idNumberRequest));
    }

    @Test
    void wontUpdateClientCase2() {
        //Given
        Client clientByJson = createNewClientWithSameData(client);
        clientByJson.setIdType(client2.getIdType());//Data from another client present in DB
        clientByJson.setIdNumber(client2.getIdNumber());//This too

        List<Client> clients = new ArrayList<>(Arrays.asList(client, client2));
        ClientImageDto clientImageDto = clientMapper.toDto(clientByJson, image);
        String idTypeRequest = "CC";
        Long idNumberRequest = 4L;

        //When
        when(clientRepository.findAll()).thenReturn(clients);

        //Then
        assertThrows(ClientNotFoundException.class,
                () -> clientService.updateClientReview(clientImageDto, idTypeRequest, idNumberRequest));
    }

    @Test
    void wontUpdateClientCase3() {
        //Given
        Client clientByJson = createNewClientWithSameData(client);
        clientByJson.setIdType(client2.getIdType());//Data from another client present in DB
        clientByJson.setIdNumber(client2.getIdNumber());//This too

        List<Client> clients = new ArrayList<>(Arrays.asList(client, client2));
        ClientImageDto clientImageDto = clientMapper.toDto(clientByJson, image);
        String idTypeRequest = "CC";
        Long idNumberRequest = 1L;

        //When
        when(clientRepository.findAll()).thenReturn(clients);

        //Then
        assertThrows(ClientUpdateException.class,
                () -> clientService.updateClientReview(clientImageDto, idTypeRequest, idNumberRequest));
    }

    @Test
    void canDeleteClient() {
        //Given
        String idTypeRequest = client.getIdType();
        Long idNumberRequest = client.getIdNumber();

        //When
        when(clientRepository.findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest)).thenReturn(Optional.of(client));
        when(imageRepository.findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest)).thenReturn(Optional.of(image));
        clientService.deleteClient(idTypeRequest, idNumberRequest);

        //Then
        verify(clientRepository).findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest);
        verify(clientRepository).deleteByClientId(client.getClientId());

        verify(imageRepository).findFirstByIdTypeAndIdNumber(idTypeRequest, idNumberRequest);
        verify(imageRepository).deleteById(image.getId());
    }

    @Test
    void wontDeleteClient() {
        //Given
        String idType = client.getIdType();
        Long idNumber = client.getIdNumber();
        Optional<Client> clientNotFound = Optional.empty();

        //When
        when(clientRepository.findFirstByIdTypeAndIdNumber(idType, idNumber)).thenReturn(clientNotFound);

        //Then
        assertThrows(ClientNotFoundException.class,
                () -> clientService.deleteClient(idType, idNumber));
    }

    //Support methods for separate object references from setup values
    public Client createNewClientWithSameData(Client client) {
        Client newClient = new Client();
        newClient.setClientId(client.getClientId());
        newClient.setIdType(client.getIdType());
        newClient.setIdNumber(client.getIdNumber());
        newClient.setFirstName(client.getFirstName());
        newClient.setLastName(client.getLastName());
        newClient.setAge(client.getAge());
        newClient.setCityOfBirth(client.getCityOfBirth());
        return newClient;
    }

    public Image createNewImageWithSameData(Image image) {
        Image newImage = new Image();
        newImage.setId(image.getId());
        newImage.setIdType(image.getIdType());
        newImage.setIdNumber(image.getIdNumber());
        newImage.setImageB64(image.getImageB64());
        return newImage;
    }

}