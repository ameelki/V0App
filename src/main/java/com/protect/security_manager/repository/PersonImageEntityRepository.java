package com.protect.security_manager.repository;

import com.protect.security_manager.entity.PersonImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonImageEntityRepository extends JpaRepository<PersonImageEntity, Long> {
   Optional<PersonImageEntity> findByOwnerId(String ownerId);
}
