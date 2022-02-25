package com.pragma.pocapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.pocapp.dto.ClientImageDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired//DTO to JSON converter
    private ObjectMapper objectMapper;

    @Order(1)
    @Sql("classpath:clients.sql")
    @Test
        //GET request Test
    void canGetAllClientsWithGetRequest() throws Exception {
        // Given
        // The application context

        // When
        final ResultActions result = mockMvc.perform(get("/api/clients"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName",
                        is("user1")));
    }

    @Order(2)
    @Test
        //POST request Test
    void canRegisterClientWithPostRequest() throws Exception {
        // Given
        // Real application context
        ClientImageDto clientImageDto = new ClientImageDto();
        clientImageDto.setFirstName("user3");
        clientImageDto.setLastName("lastname3");
        clientImageDto.setIdType("TI");
        clientImageDto.setIdNumber(1234L);
        clientImageDto.setAge(40);
        clientImageDto.setCityOfBirth("city3");
        clientImageDto.setImageB64("qwerty");

        // When
        final ResultActions result = mockMvc.perform(post("/api")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(
                        clientImageDto
                )));

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(clientImageDto.getFirstName())));
    }
}
