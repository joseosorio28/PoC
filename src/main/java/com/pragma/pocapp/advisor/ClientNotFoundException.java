package com.pragma.pocapp.advisor;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String idType,Long idNumber) {
        super("Could not find client with id: " + idType+" "+idNumber);
    }
}

