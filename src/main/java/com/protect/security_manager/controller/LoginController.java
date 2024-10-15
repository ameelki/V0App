package com.protect.security_manager.controller;

import com.protect.security_manager.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import security.manager.api.LoginApiController;
import security.manager.model.AccessTokenAuthorization;
import security.manager.model.LoginFormRequest;

@RestController
@RequestMapping("/user")
public class LoginController extends LoginApiController {

    @Autowired
    LoginService loginService;
    public LoginController(NativeWebRequest request) {
        super(request);
    }


@Override
@RequestMapping(
        method = RequestMethod.POST,
        value = "/login",
        produces = { "application/json" },
        consumes = { "application/json" }
)
    public ResponseEntity<AccessTokenAuthorization> getToken(
            @Valid @RequestBody LoginFormRequest loginFormRequest
    ) {
        AccessTokenAuthorization accessTokenAuthorization=this.loginService.getToken(loginFormRequest);
     return ResponseEntity.ok().body(accessTokenAuthorization);
    }
}
