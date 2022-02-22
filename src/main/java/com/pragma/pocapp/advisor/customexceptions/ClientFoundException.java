package com.pragma.pocapp.advisor.customexceptions;

public class ClientFoundException extends RuntimeException {

    public ClientFoundException(String idType, Long idNumber) {
        super("Client with id: " + idType+" "+idNumber+" is already in the database");
    }

}

