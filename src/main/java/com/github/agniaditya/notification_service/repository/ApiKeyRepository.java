package com.github.agniaditya.notification_service.repository;

import com.github.agniaditya.notification_service.entity.API_Keys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<API_Keys, String> {
    Optional<API_Keys> findByKeyHash(String keyHash);
}