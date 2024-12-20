package com.protect.security_manager.repository;

import com.protect.security_manager.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
    boolean existsByCode(String code);
    void deleteByCode(String code);  // Custom delete method


}
