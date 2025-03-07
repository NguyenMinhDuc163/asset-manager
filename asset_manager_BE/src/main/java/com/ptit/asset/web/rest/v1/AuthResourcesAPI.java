package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.LoginRequestDTO;
import com.ptit.asset.dto.request.RegisterRequestDTO;
import com.ptit.asset.dto.request.ResetPasswordRequestDTO;
import com.ptit.asset.service.endservice.UserService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto){
        logger.info(">>>>> Rest request to login >>>>> : {}", dto);
        val result = userService.login(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Login Success", 200,
                        "Failure login", 400), HttpStatus.OK
        );
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO dto){
        logger.info(">>>>> Rest request to register >>>>> : {}", dto);
        val result = userService.register(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Register Success", 200,
                        "Register failure", 400), HttpStatus.OK
        );
    }

    @PostMapping("/auth/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO dto){
        logger.info(">>>>> Rest request to register >>>>> : {}", dto);
        val result = userService.resetPassword(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Reset password Success", 200,
                    "Reset password failure", 400), HttpStatus.OK
        );
    }





}
