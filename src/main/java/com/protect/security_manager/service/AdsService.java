package com.protect.security_manager.service;

import com.protect.security_manager.entity.Ads;
import com.protect.security_manager.repository.AdsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdsService {

    private final AdsRepository adsRepository;

    public AdsService(AdsRepository adsRepository) {
        this.adsRepository = adsRepository;
    }

    public List<Ads> getAllAds() {
        return adsRepository.findAll();
    }

    public Optional<Ads> getAdById(Long id) {
        return adsRepository.findById(id);
    }

    public Ads createAd(Ads ad) {
        return adsRepository.save(ad);
    }

    public Ads updateAd(Long id, Ads adDetails) {
        return adsRepository.findById(id).map(ad -> {
            ad.setStartDate(adDetails.getStartDate());
            ad.setEndDate(adDetails.getEndDate());
            ad.setPublishedDate(adDetails.getPublishedDate());
            ad.setMarkedForPublish(adDetails.isMarkedForPublish());
            return adsRepository.save(ad);
        }).orElseThrow(() -> new RuntimeException("Ad not found with id " + id));
    }

    public void deleteAd(Long id) {
        adsRepository.deleteById(id);
    }
}