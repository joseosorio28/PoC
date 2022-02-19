package com.pragma.pocapp.mapper;

import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);
    private static Client client, client2;
    private static Image image, image2;
    private static ClientImageDto clientImageDto;

    @BeforeAll
    public static void setUp() {

        //Given:
        client = new Client(1L, "firstname1", "lastname1",
                "CC1", 1L, 30, "city1");

        image = new Image("1", "qwerty1", client.getIdType(), client.getIdNumber());

        client2 = new Client(2L, "firstname2", "lastname2",
                "CC2", 2L, 32, "city2");

        image2 = new Image("2", "qwerty2", client2.getIdType(), client2.getIdNumber());
    }

    @Test
    void itShouldMapToClient() {

        //Test dto to client
        //When
        clientImageDto = clientMapper.toDto(client, image);
        Client newClient = clientMapper.toClient(clientImageDto);

        //Then
        assertEquals(clientImageDto.getClientId(), newClient.getClientId());
        assertEquals(clientImageDto.getFirstName(), newClient.getFirstName());
        assertEquals(clientImageDto.getLastName(), newClient.getLastName());
        assertEquals(clientImageDto.getIdType(), newClient.getIdType());
        assertEquals(clientImageDto.getIdNumber(), newClient.getIdNumber());
        assertEquals(clientImageDto.getAge(), newClient.getAge());
        assertEquals(clientImageDto.getCityOfBirth(), newClient.getCityOfBirth());

        //Test null dto to client
        //When
        newClient = clientMapper.toClient(null);

        //Then
        assertNull(newClient);

    }

    @Test
    void itShouldMapToImage() {

        //Test dto to image
        //When
        clientImageDto = clientMapper.toDto(client, image);
        Image newImage = clientMapper.toImage(clientImageDto);

        //Then
        assertEquals(clientImageDto.getId(), newImage.getId());
        assertEquals(clientImageDto.getImageB64(), newImage.getImageB64());
        assertEquals(clientImageDto.getIdType(), newImage.getIdType());
        assertEquals(clientImageDto.getIdNumber(), newImage.getIdNumber());

        //Test null dto to image
        //When
        newImage = clientMapper.toImage(null);
        //Then
        assertNull(newImage);

    }

    @Test
    void itShouldMapToDto() {
        //Test entities to dto
        //When
        clientImageDto = clientMapper.toDto(client, image);
        //Then
        assertEquals(client.getClientId(), clientImageDto.getClientId());
        assertEquals(client.getFirstName(), clientImageDto.getFirstName());
        assertEquals(client.getLastName(), clientImageDto.getLastName());
        assertEquals(client.getIdType(), clientImageDto.getIdType());
        assertEquals(client.getIdNumber(), clientImageDto.getIdNumber());
        assertEquals(client.getAge(), clientImageDto.getAge());
        assertEquals(client.getCityOfBirth(), clientImageDto.getCityOfBirth());
        assertEquals(image.getId(), clientImageDto.getId());
        assertEquals(image.getIdType(), clientImageDto.getIdType());
        assertEquals(image.getIdNumber(), clientImageDto.getIdNumber());
        assertEquals(image.getImageB64(), clientImageDto.getImageB64());

        //Test null entities to dto
        //When
        clientImageDto = clientMapper.toDto(null, null);
        //Then
        assertNull(clientImageDto);

        //Test client and null image to dto
        //When
        clientImageDto = clientMapper.toDto(client, null);
        //Then
        assertNull(clientImageDto.getId());
        assertNotNull(clientImageDto.getIdType());
        assertNotNull(clientImageDto.getIdNumber());
        assertNull(clientImageDto.getImageB64());

        //Test image and null client to dto
        //When
        clientImageDto = clientMapper.toDto(null, image);
        //Then
        assertNull(clientImageDto.getClientId());
        assertNull(clientImageDto.getFirstName());
        assertNull(clientImageDto.getLastName());
        assertNull(clientImageDto.getIdType());
        assertNull(clientImageDto.getIdNumber());
        assertNull(clientImageDto.getAge());
        assertNull(clientImageDto.getCityOfBirth());
        assertNull(clientImageDto.getIdType());
        assertNull(clientImageDto.getIdNumber());

    }

    @Test
    void itShouldMapToDtos() {

        //Test with list of clients and related images
        //Given
        List<Client> clients = new ArrayList<>(Arrays.asList(client, client2));
        List<Image> images = new ArrayList<>(Arrays.asList(image, image2));
        //When
        List<ClientImageDto> clientImageDtos = clientMapper.toDtos(clients, images);
        //Then
        assertEquals(2, clientImageDtos.size());


        //Test with null clients
        //When
        clientImageDtos = clientMapper.toDtos(null, images);
        //Then
        assertEquals(Collections.emptyList(), clientImageDtos);

        //Test with null images
        //When
        clientImageDtos = clientMapper.toDtos(clients, null);
        //When
        assertEquals(2, clientImageDtos.size());

        //Test with empty list of images
        //Given
        clients = new ArrayList<>(Arrays.asList(client, client2));
        images = new ArrayList<>();
        //When
        clientImageDtos = clientMapper.toDtos(clients, images);
        //When
        assertEquals(2, clientImageDtos.size());

        //Test with images not completely related to clients
        //Given
        images = new ArrayList<>(Arrays.asList(
                new Image("2", "qwerty2", "TI", 3L),
                new Image("3", "qwerty", "CC1", 3L),
                new Image("4", "qwerty", "TI", 1L)
        ));
        //When
        clientImageDtos = clientMapper.toDtos(clients, images);
        //Then
        assertEquals(2, clientImageDtos.size());

    }
}