package com.pragma.pocapp.repository;

import com.pragma.pocapp.entity.ClientImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientImageRepository extends JpaRepository<ClientImage, Long> {

}