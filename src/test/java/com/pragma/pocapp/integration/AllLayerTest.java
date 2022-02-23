package com.pragma.pocapp.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.pocapp.controller.ClientController;
import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.entity.Image;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.repository.ClientRepository;
import com.pragma.pocapp.services.ClientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AllLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientService clientService;

    private final ClientMapper clientMapper = ClientMapper.INSTANCE;
    private static Client client, client2;
    private static Image image;

    @BeforeAll
    static void beforeAll() {
        //Given:
        client = new Client(null, "firstname1", "lastname1",
                "CC", 1L, 30, "city1");

        image = new Image(null, "qwerty1", client.getIdType(), client.getIdNumber());

        client2 = new Client(null, "firstname2", "lastname2",
                "TI", 2L, 32, "city2");

    }

    @Test
    void clientRegistrationWorksThroughAllLayers() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .contentType("application/json")
                        //.param("sendWelcomeMail", "true")
                        .content(objectMapper.writeValueAsString(
                                clientMapper.toDto(client,image))))
                .andExpect(status().isOk());

//        List<Client> clients = clientService.getClients();
//        assertThat(userEntity.getEmail()).isEqualTo("zaphod@galaxy.net");
    }

//            mockMvc.perform(get("/api/clients")
//                        .contentType("application/json")
//                        .param("sendWelcomeMail", "true")
//                        .content(objectMapper.writeValueAsString(user)))
//            .andExpect(status().isOk());

//Converts Object to Json String
//private String convertObjectToJsonString(ClientImageDto clientImageDto) throws JsonProcessingException {
//    ObjectWriter writer = new ObjectWriter().writer().withDefaultPrettyPrinter();
//    return writer.writeValueAsString(muffin);
//}

}
