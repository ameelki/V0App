package com.protect.security_manager.controller;
import com.protect.security_manager.service.ManageAddressService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import security.manager.api.ApiUtil;
import security.manager.api.CountriesApiController;
import security.manager.model.CreateCountry201Response;
import security.manager.model.CreateCountryRequest;
import security.manager.model.GetAllCountriesAndProvinces200ResponseInner;
import security.manager.model.UpdateCountryRequest;


import java.util.List;
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AddressController extends CountriesApiController{
    @Autowired
    ManageAddressService manageAddressService;

   public AddressController(NativeWebRequest request) {
        super(request);
    }

   public ResponseEntity<List<GetAllCountriesAndProvinces200ResponseInner>> getAllCountriesAndProvinces(){
         List<GetAllCountriesAndProvinces200ResponseInner> getCountries200ResponseInners=manageAddressService.getCountries();
         return ResponseEntity.ok().body(getCountries200ResponseInners);

    }

    @PreAuthorize("@authorizationService.hasRole(#authorization, 'superAdmin')")
    public ResponseEntity<CreateCountry201Response> createCountry(
            @NotNull @Parameter(name = "Authorization", description = "Bearer token for authorization", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization,
            @Parameter(name = "CreateCountryRequest", description = "Country object that needs to be created", required = true) @Valid @RequestBody CreateCountryRequest createCountryRequest
    )
    {
      CreateCountry201Response createCountry201Response= this.manageAddressService.createCountry(createCountryRequest.getCode(),createCountryRequest.getName());
      return ResponseEntity.ok().body(createCountry201Response);
    }


    public ResponseEntity<UpdateCountryRequest> updateCountry(
            @Parameter(name = "code", description = "Code of the country to update", required = true, in = ParameterIn.PATH) @PathVariable("code") String code,
            @Parameter(name = "UpdateCountryRequest", description = "Country object that needs to be updated", required = true) @Valid @RequestBody UpdateCountryRequest updateCountryRequest
    )
    {
        UpdateCountryRequest updateCountryRequest1=this.manageAddressService.updateCountry(code,updateCountryRequest);

        return ResponseEntity.ok().body(updateCountryRequest1);


    }

}
