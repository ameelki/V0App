package com.protect.security_manager.repository;

import com.protect.security_manager.entity.AddsImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddsImageEntityRepository extends JpaRepository<AddsImageEntity, Long> {
}