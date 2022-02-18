package com.pragma.pocapp.advisor;

public class ClientByAgeNotFoundException extends RuntimeException {

    public ClientByAgeNotFoundException(Integer age) {
        super("Could not find client with age greater than " + age);
    }
}

