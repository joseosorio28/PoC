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

        client = new Client(
                1L,
                "firstname1",
                "lastname1",
                "CC1",
                1L,
                30,
                "city1");

        image = new Image(
                "1",
                "qwerty1",
                client.getIdType(),
                client.getIdNumber());

        client2 = new Client(
                2L,
                "firstname2",
                "lastname2",
                "CC2",
                2L,
                32,
                "city2");

        image2 = new Image(
                "2",
                "qwerty2",
                client2.getIdType(),
                client2.getIdNumber());
    }

    @Test
    void toClient() {

        //Test dto to client
        clientImageDto = clientMapper.toDto(client, image);
        Client newClient = clientMapper.toClient(clientImageDto);

        assertEquals(clientImageDto.getClientId(),newClient.getClientId());
        assertEquals(clientImageDto.getFirstName(),newClient.getFirstName());
        assertEquals(clientImageDto.getLastName(),newClient.getLastName());
        assertEquals(clientImageDto.getIdType(),newClient.getIdType());
        assertEquals(clientImageDto.getIdNumber(),newClient.getIdNumber());
        assertEquals(clientImageDto.getAge(),newClient.getAge());
        assertEquals(clientImageDto.getCityOfBirth(),newClient.getCityOfBirth());

        //Test null dto to client
        newClient = clientMapper.toClient(null);

        assertNull(newClient);

    }

    @Test
    void toImage() {

        //Test dto to image
        clientImageDto = clientMapper.toDto(client, image);
        Image newImage = clientMapper.toImage(clientImageDto);

        assertEquals(clientImageDto.getId(),newImage.getId());
        assertEquals(clientImageDto.getImageB64(),newImage.getImageB64());
        assertEquals(clientImageDto.getIdType(),newImage.getIdType());
        assertEquals(clientImageDto.getIdNumber(),newImage.getIdNumber());

        //Test null dto to image
        newImage = clientMapper.toImage(null);

        assertNull(newImage);

    }

    @Test
    void toDto() {
        //Test entities to dto
        clientImageDto = clientMapper.toDto(client, image);

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
        clientImageDto = clientMapper.toDto(null, null);
        assertNull(clientImageDto);

        //Test client and null image to dto
        clientImageDto = clientMapper.toDto(client, null);

        assertNull(clientImageDto.getId());
        assertNotNull(clientImageDto.getIdType());
        assertNotNull(clientImageDto.getIdNumber());
        assertNull(clientImageDto.getImageB64());

        //Test image and null client to dto
        clientImageDto = clientMapper.toDto(null, image);

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
    void toDtos() {

        //Test with list of clients and related images
        List<Client> clients = new ArrayList<>(Arrays.asList(client,client2));
        List<Image> images = new ArrayList<>(Arrays.asList(image,image2));

        List<ClientImageDto> clientImageDtos = clientMapper.toDtos(clients,images);

        assertEquals(2,clientImageDtos.size());


        //Test with null clients
        clientImageDtos = clientMapper.toDtos(null,images);
        assertEquals(Collections.emptyList(),clientImageDtos);

        //Test with null images
        clientImageDtos = clientMapper.toDtos(clients,null);
        assertEquals(2,clientImageDtos.size());

        //Test with empty list of images
        clients = new ArrayList<>(Arrays.asList(client,client2));
        images = new ArrayList<>();

        clientImageDtos = clientMapper.toDtos(clients,images);
        assertEquals(2,clientImageDtos.size());

        //Test with images not completely related to clients
        images = new ArrayList<>(Arrays.asList(
                new Image("2","qwerty2","TI",3L),
                new Image("3","qwerty","CC1",3L),
                new Image("4","qwerty","TI",1L)
                ));

        clientImageDtos = clientMapper.toDtos(clients,images);
        assertEquals(2,clientImageDtos.size());

    }
}