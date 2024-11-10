package com.protect.security_manager.dataLoader;

import com.protect.security_manager.entity.Ads;
import com.protect.security_manager.entity.Country;
import com.protect.security_manager.entity.Province;
import com.protect.security_manager.repository.AdsRepository;
import com.protect.security_manager.repository.CountryRepository;
import com.protect.security_manager.repository.ProvinceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    public AdsRepository adsRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        provinceRepository.deleteAll();
        countryRepository.deleteAll();
        entityManager.createNativeQuery("ALTER SEQUENCE countries_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE provinces_id_seq RESTART WITH 1").executeUpdate();
        Country france = new Country("FR", "France");
        Country belgique = new Country("BE", "Belgique");
        Country germany = new Country("DE", "Germany");
        Country luxembourg = new Country("LU", "Luxembourg");
        Country maroc = new Country("MA", "Maroc");
        Country tunisie = new Country("TN", "Tunisia");
        Country portugal = new Country("PT", "Portugal");
        Country espagne = new Country("ES", "Espagne"); // Ajout de l'Espagne
        countryRepository.save(france);
        countryRepository.save(belgique);
        countryRepository.save(germany);
        countryRepository.save(luxembourg); // Sauvegarde du Luxembourg
        countryRepository.save(maroc);
        countryRepository.save(tunisie);
        countryRepository.save(portugal);
        countryRepository.save(espagne); // Sauvegarde de l'Espagne
        provinceRepository.save(new Province("IDF", "Île-de-France", france)); // Paris
        provinceRepository.save(new Province("PAC", "Provence-Alpes-Côte d'Azur", france)); // Marseille
        provinceRepository.save(new Province("AUR", "Auvergne-Rhône-Alpes", france)); // Lyon
        provinceRepository.save(new Province("MON", "Monaco", france)); // Monaco
        provinceRepository.save(new Province("OCC", "Occitanie", france)); // Toulouse
        provinceRepository.save(new Province("LOR", "Lorraine", france)); // Metz
        provinceRepository.save(new Province("BRU", "Bruxelles-Capitale", belgique));
        provinceRepository.save(new Province("WAL", "Wallonie", belgique));
        provinceRepository.save(new Province("FLA", "Flandre", belgique));
        provinceRepository.save(new Province("BY", "Bavière", germany));
        provinceRepository.save(new Province("BE", "Berlin", germany));
        provinceRepository.save(new Province("BW", "Baden-Württemberg", germany));
        provinceRepository.save(new Province("HH", "Hambourg", germany));
        provinceRepository.save(new Province("HE", "Hesse", germany));
        provinceRepository.save(new Province("NI", "Basse-Saxe", germany));
        provinceRepository.save(new Province("NW", "Rhénanie-du-Nord-Westphalie", germany));
        provinceRepository.save(new Province("RP", "Rhénanie-Palatinat", germany));
        provinceRepository.save(new Province("SH", "Schleswig-Holstein", germany));
        provinceRepository.save(new Province("SN", "Saxe", germany));
        provinceRepository.save(new Province("MUC", "München", germany)); // Munich
        provinceRepository.save(new Province("BER", "Berlin", germany)); // Berlin
        provinceRepository.save(new Province("HAM", "Hamburg", germany)); // Hambourg
        provinceRepository.save(new Province("FRA", "Frankfurt am Main", germany)); // Francfort
        provinceRepository.save(new Province("KOL", "Köln", germany)); // Cologne
        provinceRepository.save(new Province("STU", "Stuttgart", germany)); // Stuttgart
        provinceRepository.save(new Province("DÜS", "Düsseldorf", germany)); // Düsseldorf
        provinceRepository.save(new Province("DORT", "Dortmund", germany)); // Dortmund
        provinceRepository.save(new Province("ESS", "Essen", germany)); // Essen
        provinceRepository.save(new Province("LEI", "Leipzig", germany)); // Leipzig
        provinceRepository.save(new Province("LUX", "Luxembourg", luxembourg));
        provinceRepository.save(new Province("ECH", "Echternach", luxembourg));
        provinceRepository.save(new Province("ETZ", "Ettelbruck", luxembourg));
        provinceRepository.save(new Province("DIE", "Diekirch", luxembourg));
        provinceRepository.save(new Province("MAL", "Mallek", luxembourg));
        provinceRepository.save(new Province("DUR", "Dudelange", luxembourg));
        provinceRepository.save(new Province("FRA", "Frisange", luxembourg));
        provinceRepository.save(new Province("LUX", "Luxembourg", luxembourg));
        provinceRepository.save(new Province("SAN", "Sanem", luxembourg));
        provinceRepository.save(new Province("TET", "Tétange", luxembourg));
        provinceRepository.save(new Province("WET", "Wetteren", luxembourg));
        provinceRepository.save(new Province("AER", "Aerden", luxembourg));
        provinceRepository.save(new Province("CAS", "Casablanca", maroc));
        provinceRepository.save(new Province("RAB", "Rabat", maroc));
        provinceRepository.save(new Province("MAR", "Marrakech", maroc));
        provinceRepository.save(new Province("TUN", "Tunis", tunisie));
        provinceRepository.save(new Province("SFAX", "Sfax", tunisie));
        provinceRepository.save(new Province("SOUS", "Sousse", tunisie));
        provinceRepository.save(new Province("LIS", "Lisboa", portugal));
        provinceRepository.save(new Province("POR", "Porto", portugal));
        provinceRepository.save(new Province("ALG", "Algarve", portugal));
        provinceRepository.save(new Province("MAD", "Madrid", espagne)); // Madrid
        provinceRepository.save(new Province("BAR", "Barcelone", espagne)); // Barcelone
        provinceRepository.save(new Province("SEV", "Séville", espagne)); // Séville
        LocalDate today = LocalDate.now();

        // Cas 1: Annonce actuellement dans la plage de dates (doit être publiée)
        Ads adInDateRange = new Ads(today.minusDays(5), today.plusDays(5), false);

        // Cas 2: Annonce expirée (doit être dépubliée)
        Ads adExpired = new Ads(today.minusDays(10), today.minusDays(1), true);

        // Cas 3: Annonce future (ne doit pas être publiée)
        Ads adFuture = new Ads(today.plusDays(2), today.plusDays(10), false);

        // Cas 4: Annonce avec dates de début et de fin égales à aujourd'hui (doit être publiée)
        Ads adExactToday = new Ads(today, today, false);

        // Enregistrement des annonces
        adsRepository.saveAll(Arrays.asList(adInDateRange, adExpired, adFuture, adExactToday));

    }




}
