package com.pragma.pocapp.controller;


import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
@Validated
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientImageDto>> listAllClients() {
        return new ResponseEntity<>(clientService.getClients(), HttpStatus.FOUND);
    }

    @GetMapping("/clientsbyage")
    public ResponseEntity<List<ClientImageDto>> listClientsByAge(
            @RequestParam(name = "age") Integer age) {
        return new ResponseEntity<>(clientService.getClientsByAge(age), HttpStatus.FOUND);
    }

    @GetMapping("client")
    public ResponseEntity<ClientImageDto> getClient(
            @RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") Long idNumber) {
        return new ResponseEntity<>(clientService.searchClient(idType, idNumber), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<ClientImageDto> registerClient(@Valid @RequestBody ClientImageDto clientImageDto) {
        clientService.addClient(clientImageDto);
        return new ResponseEntity<>(clientImageDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ClientImageDto> updateClient(
            @Valid @RequestBody ClientImageDto clientImageDto,
            @RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") Long idNumber) {
        return new ResponseEntity<>(clientService.updateClient(clientImageDto,idType,idNumber), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("client")
    public ResponseEntity<String> deleteClient(@RequestParam String idType, @RequestParam Long idNumber) {
        clientService.deleteClient(idType, idNumber);
        return new ResponseEntity<>("User deleted", HttpStatus.ACCEPTED);
    }

}
