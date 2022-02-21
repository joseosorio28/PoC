package com.pragma.pocapp.repository;

import com.pragma.pocapp.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAll();
    Optional<Client> findFirstByIdTypeAndIdNumber(String idType, Long idNumber);
    Optional<List<Client>>  findByAgeGreaterThan(Integer age);
    void deleteByClientId(Long clientId);
}
