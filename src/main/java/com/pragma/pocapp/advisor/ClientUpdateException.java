package com.pragma.pocapp.advisor;

public class ClientUpdateException extends RuntimeException {

    public ClientUpdateException(String idTypeRequest, Long idNumberRequest,
                                 String idTypeInJson, Long idNumberInJson) {
        super("Client requested with id: " + idTypeRequest+" "+idNumberRequest+" is trying to" +
                " update its data with and id: " + idTypeInJson+" "+idNumberInJson+" already taken in the database" +
                " by other client");
    }

}

