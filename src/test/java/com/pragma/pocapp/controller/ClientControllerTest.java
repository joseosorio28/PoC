package com.pragma.pocapp.controller;

import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.services.ClientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @InjectMocks
    ClientController clientController;

    @Mock
    ClientService clientService;

    private final ClientMapper clientMapper = ClientMapper.INSTANCE;
    private static Client client, client2;
    private static Image image, image2;

    @BeforeAll
    static void beforeAll() {
        //Given:
        client = new Client(1L, "firstname1", "lastname1",
                "CC1", 1L, 30, "city1");

        image = new Image("1", "qwerty1", client.getIdType(), client.getIdNumber());

        client2 = new Client(2L, "firstname2", "lastname2",
                "CC2", 2L, 32, "city2");

        image2 = new Image("2", "qwerty2", client2.getIdType(), client2.getIdNumber());

    }

    @BeforeEach
    void setUp() {
        clientController = new ClientController(clientService);
    }

    @Test
    void canListAllClients() {
        //Given
        List<Client> clients = new ArrayList<>(Arrays.asList(client, client2));
        List<Image> images = new ArrayList<>(Arrays.asList(image, image2));

        //When
        when(clientService.getClients()).thenReturn(
                clientMapper.toDtos(clients, images));
        ResponseEntity<List<ClientImageDto>> response = clientController.listAllClients();

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(clientService).getClients();
    }

    @Test
    void canListClientsByAge() {
        //Given
        Integer age = 10;
        List<Client> clients = new ArrayList<>(Arrays.asList(client, client2));
        List<Image> images = new ArrayList<>(Arrays.asList(image, image2));

        //When
        when(clientService.getClientsByAge(age)).thenReturn(
                clientMapper.toDtos(clients, images));
        ResponseEntity<List<ClientImageDto>> response = clientController.listClientsByAge(age);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(clientService).getClientsByAge(age);
    }

    @Test
    void canGetClient() {
        //Given
        String idType = client.getIdType();
        Long idNumber = client.getIdNumber();

        //When
        when(clientService.searchClient(idType, idNumber)).thenReturn(
                clientMapper.toDto(client, image));
        ResponseEntity<ClientImageDto> response = clientController.getClient(idType, idNumber);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(clientService).searchClient(idType, idNumber);
    }

    @Test
    void canRegisterClient() {
        //Given
        ClientImageDto clientImageDto = clientMapper.toDto(client, image);

        //When
        ResponseEntity<ClientImageDto> response = clientController.registerClient(clientImageDto);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        verify(clientService).addClient(clientImageDto);
    }

    @Test
    void canUpdateClient() {
        //Given
        String idType = client.getIdType();
        Long idNumber = client.getIdNumber();
        ClientImageDto clientImageDto = clientMapper.toDto(client, image);

        //When
        ResponseEntity<ClientImageDto> response
                = clientController.updateClient(clientImageDto, idType, idNumber);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
        verify(clientService).updateClientReview(clientImageDto, idType, idNumber);
    }

    @Test
    void canDeleteClient() {
        //Given
        String idType = client.getIdType();
        Long idNumber = client.getIdNumber();

        //When
        ResponseEntity<String> response = clientController.deleteClient(idType, idNumber);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
        verify(clientService).deleteClient(idType, idNumber);
    }
}