package com.pragma.pocapp.repository;

import com.pragma.pocapp.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByClientId(Long clientId);
    void deleteByClientId(Long clientId);
}