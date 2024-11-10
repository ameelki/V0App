package com.protect.security_manager.repository;

import com.protect.security_manager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
    // Ajoutez des méthodes personnalisées si nécessaire
}