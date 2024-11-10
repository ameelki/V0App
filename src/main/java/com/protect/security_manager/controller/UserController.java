package com.protect.security_manager.controller;

import com.protect.security_manager.service.LoginService;
import com.protect.security_manager.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import security.manager.api.UserApiController;
import security.manager.model.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
// Base path for all endpoints in this controller
public class UserController extends UserApiController {
   @Autowired
   private UserService userService;
   @Autowired
   private LoginService loginService;


    public UserController(NativeWebRequest request) {
        super(request);
    }

    //@Override
    public ResponseEntity<CreateUser200Response> createUser(@Valid @RequestBody User body) {
        CreateUser200Response createUser200Response=this.userService.createUser(body);
        return ResponseEntity.ok().body(createUser200Response);
    }


    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/user",
            consumes = { "application/json" }
    )

    public ResponseEntity<Void> updateUser(
            @Parameter(name = "PasswordResetRequest", description = "", required = true) @Valid @RequestBody PasswordResetRequest passwordResetRequest
    ) {
        this.userService.resetPassword(passwordResetRequest);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    public ResponseEntity<UserSummaryWithoutSensitiveData> getUserByEmail(
            @Parameter(name = "EmailRequest", description = "", required = true) @Valid @RequestBody EmailRequest emailRequest
    )
    {
        UserSummaryWithoutSensitiveData user=this.userService.getUserByEmail(emailRequest.getEmail());
        return  ResponseEntity.ok().body(user);

    }


   /* public ResponseEntity<User> getUserByEmail(
            @Parameter(name = "EmailRequest", description = "", required = true) @Valid @RequestBody EmailRequest emailRequest
    ) {
         User user=this.userService.getUserByEmail(emailRequest.getEmail());
        return  ResponseEntity.ok().body(user);

    }*/


    @GetMapping("/user")
    @PreAuthorize("@authorizationService.hasRole(#token, 'superAdmin')")
    public ResponseEntity<String> userEndpoint(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        return new ResponseEntity<>("UserEntity content", HttpStatus.OK);
    }




    public ResponseEntity<UserSummary> updateUserById(
            @Parameter(name = "userId", description = "ID of the user to be updated", required = true, in = ParameterIn.PATH) @PathVariable("userId") String userId,
            @Parameter(name = "tokenSubId", description = "Token subject ID for additional validation or context", required = true, in = ParameterIn.PATH) @PathVariable("tokenSubId") String tokenSubId,
            @Parameter(name = "User", description = "", required = true) @Valid @RequestBody User user
    ) {
        this.userService.updateUser(userId,tokenSubId,user);
        return new ResponseEntity<>(HttpStatus.OK);

    }



}