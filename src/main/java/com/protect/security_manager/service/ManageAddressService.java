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
import security.manager.model.GetCountries200ResponseInner;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManageAddressService {

    @Autowired
    CountryRepository countryRepository;

    public List<GetCountries200ResponseInner> getCountries() {
        List<Country> countries = countryRepository.findAll();

        if (countries.isEmpty()) {
            throw new ResourceNotFoundException("No countries found.");
        }

        return countries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Méthode privée pour mapper Country à GetCountries200ResponseInner
    private GetCountries200ResponseInner mapToResponse(Country country) {
        GetCountries200ResponseInner response = new GetCountries200ResponseInner();
        response.setCode(country.getCode());  // Assurez-vous que getId() renvoie le code du pays
        response.setName(country.getName()); // Assurez-vous que getName() renvoie le nom du pays
        return response;
    }



}
