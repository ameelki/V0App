package com.protect.security_manager.Mapper;

import com.protect.security_manager.entity.Country;
import org.mapstruct.Mapper;
import security.manager.model.CreateCountryRequest;
import security.manager.model.UpdateCountryRequest;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    UpdateCountryRequest CountryToUpdateCountryRequest(Country country);
    CreateCountryRequest countryToCreateCountryRequest(Country country);
}

