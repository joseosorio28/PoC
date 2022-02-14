package com.pragma.pocapp.controller;


import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.mapper.ClientMapper;
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
    private final ClientMapper clientMapper;

    @Autowired
    public ClientController(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @PostMapping
    public ResponseEntity<ClientImageDto> registerClient(@Valid @RequestBody ClientImageDto clientImageDto) {
        clientService.addClient(clientMapper.toEntity(clientImageDto));
        return new ResponseEntity<>(clientImageDto,HttpStatus.CREATED);
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientImageDto>> listAllClients(){
        return new ResponseEntity<>(clientMapper.toDtos(clientService.getClients()),HttpStatus.FOUND);
    }

    @GetMapping("client")
    public ResponseEntity<ClientImageDto> getClient(
            @RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") Long idNumber){
        ClientImageDto clientToGet = clientMapper.toDto(clientService.searchClient(idType,idNumber));
        return new ResponseEntity<>(clientToGet,HttpStatus.FOUND);
    }

    @PutMapping
    public ResponseEntity<ClientImageDto> updateClient(@Valid @RequestBody ClientImageDto clientImageDto) {
        ClientImageDto updateClient = clientMapper.toDto(
                clientService.updateClient(clientMapper.toEntity(clientImageDto)));
        return new ResponseEntity<>(updateClient,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("client")
    public ResponseEntity<String> deleteClient(@RequestParam String idType, @RequestParam Long idNumber) {
        clientService.deleteClient(idType,idNumber);
        return new ResponseEntity<>("User deleted",HttpStatus.ACCEPTED);
    }

}
