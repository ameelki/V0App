package com.protect.security_manager.service;

import com.protect.security_manager.entity.Country;
import com.protect.security_manager.exception.GlobalExceptionHandler;
import com.protect.security_manager.exception.ResourceNotFoundException;
import com.protect.security_manager.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import security.manager.model.GetAllCountriesAndProvinces200ResponseInner;
import security.manager.model.GetAllCountriesAndProvinces200ResponseInnerProvincesInner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManageAddressService {

    @Autowired
    CountryRepository countryRepository;

   public List<GetAllCountriesAndProvinces200ResponseInner> getCountries() {
        List<Country> countries = countryRepository.findAll();

        if (countries.isEmpty()) {
            throw new ResourceNotFoundException("No countries found.");
        }

        return countries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //Méthode privée pour mapper Country à GetCountries200ResponseInner
    private GetAllCountriesAndProvinces200ResponseInner mapToResponse(Country country) {
        GetAllCountriesAndProvinces200ResponseInner response = new GetAllCountriesAndProvinces200ResponseInner();

        // Vérifier si le pays est null avant d'essayer de mapper ses attributs
        if (country == null) {
            return response; // Retourner une réponse vide si le pays est null
        }

        // Mapping des attributs du pays
        response.setCode(Optional.ofNullable(country.getCode()).orElse("")); // Eviter les null pour le code du pays
        response.setName(Optional.ofNullable(country.getName()).orElse("")); // Eviter les null pour le nom du pays

        // Mapping des provinces associées au pays
        response.setProvinces(mapProvinces(country));

        return response;
    }

    private List<GetAllCountriesAndProvinces200ResponseInnerProvincesInner> mapProvinces(Country country) {
        // Vérification de la liste des provinces et mapping des propriétés
        return Optional.ofNullable(country.getProvinces()) // Vérifier si la liste des provinces n'est pas null
                .orElse(Collections.emptyList()) // Si null, retourner une liste vide pour éviter les exceptions
                .stream()
                .map(province -> {
                    // Créez une instance de Province DTO (GetAllCountriesAndProvinces200ResponseInnerProvincesInner)
                    GetAllCountriesAndProvinces200ResponseInnerProvincesInner provinceResponse =
                            new GetAllCountriesAndProvinces200ResponseInnerProvincesInner();

                    // Vérifier et mapper les propriétés de la province
                    provinceResponse.setCode(Optional.ofNullable(province.getCode()).orElse("")); // Eviter les null pour le code de la province
                    provinceResponse.setName(Optional.ofNullable(province.getName()).orElse("")); // Eviter les null pour le nom de la province

                    return provinceResponse;
                })
                .collect(Collectors.toList()); // Collecter en tant que liste
    }



}
