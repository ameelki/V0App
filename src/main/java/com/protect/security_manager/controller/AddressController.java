package com.protect.security_manager.controller;

import com.protect.security_manager.repository.CountryRepository;
import com.protect.security_manager.service.ManageAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import security.manager.api.ApiUtil;
import security.manager.api.CountriesApi;
import security.manager.api.CountriesApiController;
import security.manager.model.GetCountries200ResponseInner;

import java.util.List;
@RestController
public class AddressController extends CountriesApiController {
    public AddressController(NativeWebRequest request) {
        super(request);
    }
    @Autowired
    ManageAddressService manageAddressService;

    public ResponseEntity<List<GetCountries200ResponseInner>> getCountries(){
         List<GetCountries200ResponseInner> getCountries200ResponseInners=manageAddressService.getCountries();
         return ResponseEntity.ok().body(getCountries200ResponseInners);



    }
}
