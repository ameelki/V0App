package com.protect.security_manager.controller;
import com.protect.security_manager.service.ManageAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import security.manager.api.CountriesApiController;
import security.manager.model.GetAllCountriesAndProvinces200ResponseInner;


import java.util.List;
@RestController
public class AddressController extends CountriesApiController  {
    @Autowired
    ManageAddressService manageAddressService;

    public AddressController(NativeWebRequest request) {
        super(request);
    }

    public ResponseEntity<List<GetAllCountriesAndProvinces200ResponseInner>> getAllCountriesAndProvinces(){
         List<GetAllCountriesAndProvinces200ResponseInner> getCountries200ResponseInners=manageAddressService.getCountries();
         return ResponseEntity.ok().body(getCountries200ResponseInners);



    }
}
