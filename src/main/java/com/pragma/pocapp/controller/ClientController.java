package com.pragma.pocapp.controller;


import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import com.pragma.pocapp.mapper.ClientMapper;
import com.pragma.pocapp.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientController(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @PostMapping
    public ResponseEntity<ClientImageDto> registerClient(@Valid @RequestBody ClientImageDto clientImageDto) {
//        System.out.println(clientImageDto);
//        System.out.println("==============");
//        System.out.println(clientMapper.toEntity(clientImageDto));
        clientService.addClient(clientMapper.toEntity(clientImageDto));
        return new ResponseEntity<>(clientImageDto,HttpStatus.CREATED);
    }

//    @GetMapping("/clients")
//    public List<Client> listAllClients(){
//        return clientService.getClients();
//    }
//
//    @GetMapping("client")
//    public ResponseEntity<Client> getClient(
//            @RequestParam(name = "idType") String idType,
//            @RequestParam(name = "idNumber") Long idNumber){
//        Client clientToGet = clientService.searchClient(idType,idNumber);
//        return new ResponseEntity<>(clientToGet,HttpStatus.FOUND);
//    }
//
//    @PostMapping
//    public ResponseEntity<Client> registerClient(@Valid @RequestBody Client client) {
//        clientService.addClient(client);
//        return new ResponseEntity<>(client,HttpStatus.CREATED);
//    }
//
//    @PutMapping
//    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client) {
//        clientService.updateClient(client);
//        return new ResponseEntity<>(client,HttpStatus.CREATED);
//    }
//
//    @DeleteMapping("client")
//    public ResponseEntity<String> deleteClient(@RequestParam String idType, @RequestParam Long idNumber) {
//        clientService.deleteClient(idType,idNumber);
//        return new ResponseEntity<>("User deleted",HttpStatus.ACCEPTED);
//    }

}
