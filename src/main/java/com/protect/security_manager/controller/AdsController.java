package com.protect.security_manager.controller;

import com.protect.security_manager.entity.Ads;
import com.protect.security_manager.service.AdsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping
    public ResponseEntity<List<Ads>> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ads> getAdById(@PathVariable Long id) {
        return adsService.getAdById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ads> createAd(@RequestBody Ads ad) {
        return ResponseEntity.ok(adsService.createAd(ad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ads> updateAd(@PathVariable Long id, @RequestBody Ads adDetails) {
        try {
            Ads updatedAd = adsService.updateAd(id, adDetails);
            return ResponseEntity.ok(updatedAd);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adsService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }
}