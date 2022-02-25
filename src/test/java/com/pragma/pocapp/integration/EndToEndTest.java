package com.pragma.pocapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Order(1)//Make this test first
    @Sql("classpath:clients.sql")//with four (4)  clients
    @Test
        //GET request Test
    void canGetAllClientsWithGetRequest() throws Exception {
        // Given
        // The application context
        // The (4) clients initially injected in the database, see "clients.sql" file.

        // When
        final ResultActions result = mockMvc
                .perform(
                        get("/api/clients"))
                .andDo(print());

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))// must be 4 clients
                .andExpect(jsonPath("$[0].firstName",//Review random fields of two clients
                        is("user1")))
                .andExpect(jsonPath("$[2].cityOfBirth",
                        is("city3")));
    }

    @Order(2)
    @Test
        //GET request Test
    void canGetOneClientWithGetRequest() throws Exception {
        // Given
        // The application context
        // The (4) clients initially injected in the database, see "clients.sql" file.
        String idType = "CC";//Ask for client2
        String idNumber = "5678";

        // When
        final ResultActions result = mockMvc
                .perform(
                        get("/api/client/")
                                .param("idType", idType)
                                .param("idNumber", idNumber))
                .andDo(print());

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idType",//Review client2
                        is(idType)))
                .andExpect(jsonPath("$.idNumber",
                        is(Integer.parseInt(idNumber))));
    }

    @Order(3)
    @Test
    void canGetClientsByAgeWithGetRequest() throws Exception {
        // Given
        // The application context
        // The (4) clients initially injected in the database, see "clients.sql" file.
        String age = "20";//look for clients with age greater than 20.

        // When
        final ResultActions result = mockMvc
                .perform(
                        get("/api/clientsbyage")
                                .param("age", age))
                .andDo(print());

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))// must be 2 clients greater than 20
                .andExpect(jsonPath("$[0].firstName",
                        is("user3")))
                .andExpect(jsonPath("$[0].cityOfBirth",
                        is("city3")))
                .andExpect(jsonPath("$[1].firstName",
                        is("user4")))
                .andExpect(jsonPath("$[1].cityOfBirth",
                        is("city4")));
    }

    @Order(4)
    @Test
        //POST request Test
    void canRegisterClientWithPostRequest() throws Exception {
        // Given
        // Real application context

        String jsonData = "{" +
                "\"firstName\":\"user5\"," +
                "\"lastName\":\"lastname5\"," +
                "\"idType\":\"TI\"," +
                "\"idNumber\":9876," +
                "\"age\":60," +
                "\"cityOfBirth\":\"city5\"," +
                "\"imageB64\":\"qwerty\"" +
                "}\n";

        // When
        final ResultActions result = mockMvc
                .perform(
                        post("/api")
                                .contentType("application/json")
                                .content(jsonData))
                .andDo(print());

        // Then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idType",
                        is("TI")))
                .andExpect(jsonPath("$.idNumber",
                        is(9876)));

    }

    @Order(5)
    @Test
        //PUT request Test
    void canUpdateClientWithPutRequest() throws Exception {
        // Given
        // Real application context
        // last Post request add client #5
        String idType = "TI";//Ask for client5
        String idNumber = "9876";
        String jsonData = "{" +
                "\"firstName\":\"user5\"," +
                "\"lastName\":\"lastname5\"," +
                "\"idType\":\"CC\"," +
                "\"idNumber\":4321," +
                "\"age\":90," +
                "\"cityOfBirth\":\"city5\"," +
                "\"imageB64\":\"ytrewq\"" +
                "}\n";

        // When
        final ResultActions result = mockMvc
                .perform(
                        put("/api")
                                .contentType("application/json")
                                .content(jsonData)
                                .param("idType", idType)
                                .param("idNumber", idNumber))
                .andDo(print());

        // Then
        result
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.idType",
                        is("CC")))
                .andExpect(jsonPath("$.idNumber",
                        is(4321)));

    }

    @Order(6)
    @Test
        //GET request Test
    void canDeleteOneClientWithDeleteRequest() throws Exception {
        // Given
        // The application context
        // last Post request add client #5
        String idType = "CC";//Ask for client5 who was updated in last put request
        String idNumber = "4321";

        // When
        final ResultActions result = mockMvc
                .perform(
                        delete("/api/client/")
                                .param("idType", idType)
                                .param("idNumber", idNumber))
                .andDo(print());

        // Then
        result
                .andExpect(status().isAccepted())
                .andExpect(content().string("User deleted with id: " +
                        idType + " " + idNumber))
                .andReturn();
    }


}
