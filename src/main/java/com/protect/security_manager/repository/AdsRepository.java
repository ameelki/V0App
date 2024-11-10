package com.protect.security_manager.repository;

import com.protect.security_manager.entity.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface AdsRepository extends JpaRepository<Ads, Long> {

    @Query("SELECT a FROM Ads a WHERE a.startDate <= :today AND a.endDate >= :today")
    List<Ads> findAdsByDateRange(LocalDate today);
    @Query("SELECT a FROM Ads a WHERE a.endDate < :today")
    List<Ads> findAdsOutsideDateRange(LocalDate today);
}
