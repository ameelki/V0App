package com.protect.security_manager.controller;

import com.protect.security_manager.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotNull;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import security.manager.api.ApiUtil;
import security.manager.api.UsersListApiController;
import security.manager.model.UserSummary;

import java.util.List;

@RestController
@RequestMapping("/manage")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsersController extends UsersListApiController {
    @Autowired
    private UserService userService;

    public UsersController(NativeWebRequest request) {
        super(request);
    }

    @PreAuthorize("@authorizationService.hasRole(#authorization, 'superAdmin')")
    public ResponseEntity<List<UserSummary>> getUserList(
            @RequestHeader(value = "Authorization", required = true) String authorization){
        List<UserSummary> userSummaries =this.userService.getAllUsers();
        return new ResponseEntity<>(userSummaries, HttpStatus.OK);    }

}


