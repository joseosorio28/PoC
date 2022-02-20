package com.pragma.pocapp.services;

import com.pragma.pocapp.advisor.ClientUpdateException;
import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.repository.ClientRepository;
import com.pragma.pocapp.repository.ImageRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ClientMapper clientMapper;
    private ClientService clientService;

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
        clientService = new ClientService(clientRepository, imageRepository, clientMapper);


    }

    @Test
    void canGetClients() {
        clientService.getClients();
        verify(clientRepository).findAll();
        verify(imageRepository).findAll();
    }

    @Test
    void canGetClientsByAge() {
        //Given
        int age = 10;

        clientService.getClientsByAge(age);
        verify(clientRepository).findByAgeGreaterThan(age);
        verify(imageRepository).findAll();

    }

    @Test
    void canSearchClient() {
    }

    @Test
    void canAddClient() {
    }

    @Test
    void updateClientReview() {
    }

    @Test
    void updateClient() {
    }

    @Test
    void updateImage() {
    }

    @Test
    void deleteClient() {
    }
}