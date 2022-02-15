package com.pragma.pocapp.repository;

import com.pragma.pocapp.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByIdTypeAndIdNumber(String idType, Long idNumber);

}
