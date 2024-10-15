package com.protect.security_manager.repository;

import com.protect.security_manager.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    // Vous pouvez ajouter des méthodes personnalisées ici si nécessaire
    Country findByCode(String code);
}
