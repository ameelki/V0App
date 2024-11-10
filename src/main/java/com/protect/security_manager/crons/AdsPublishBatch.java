package com.protect.security_manager.crons;

import com.protect.security_manager.entity.Ads;
import com.protect.security_manager.repository.AdsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class AdsPublishBatch {

    @Autowired
    private AdsRepository adsRepository;
    @Value("${publish.ads.cron.expression}")
    private String cronExpression;

    @Scheduled(cron = "${publish.ads.cron.expression}")
    @Transactional
    public void checkAndMarkAdsForPublishing() {
        LocalDate today = LocalDate.now();

        // Trouver les entités à publier (date dans la plage)
        List<Ads> adsToPublish = adsRepository.findAdsByDateRange(today);
        adsToPublish.forEach(ad -> ad.setMarkedForPublish(true));

        // Trouver les entités à dépublier (date de fin dépassée)
        List<Ads> adsToUnpublish = adsRepository.findAdsOutsideDateRange(today);
        adsToUnpublish.forEach(ad -> ad.setMarkedForPublish(false));

        // Sauvegarder les modifications
        adsRepository.saveAll(adsToPublish);
        adsRepository.saveAll(adsToUnpublish);

    }
}
