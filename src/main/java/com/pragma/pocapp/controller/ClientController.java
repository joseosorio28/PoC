package com.pragma.pocapp.controller;


import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public List<Client> listAllClients(){
        return clientService.getClients();
    }

    @GetMapping("client")
    public ResponseEntity<Client> getClient(
            @RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") Long idNumber){
        Client clientToGet = clientService.searchClient(idType,idNumber);
        return new ResponseEntity<>(clientToGet,HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Client> registerClient(@Valid @RequestBody Client client) {
        clientService.addClient(client);
        return new ResponseEntity<>(client,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client) {
        clientService.updateClient(client);
        return new ResponseEntity<>(client,HttpStatus.CREATED);
    }

    @DeleteMapping("client")
    public ResponseEntity<String> deleteClient(@RequestParam String idType, @RequestParam Long idNumber) {
        clientService.deleteClient(idType,idNumber);
        return new ResponseEntity<>("User deleted",HttpStatus.ACCEPTED);
    }

}
