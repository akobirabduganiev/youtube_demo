package com.company.controller;

import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.service.AuthService;
import com.company.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("auth/")
@Api("Login and registration")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ProfileService profileService;

    @PostMapping("/login")
    @ApiOperation(notes = "This method for login", value = "This method for login")
    public ResponseEntity<?> create(@RequestBody @Valid AuthDTO dto) {
        log.info("Authorization: {}", dto);
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody ProfileDTO dto) {
        log.info("registration : {}", dto);
        authService.registration(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verification/{jwt}")
    public ResponseEntity<?> verification(@PathVariable("jwt") String jwt) {
        authService.verification(jwt);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verification/email/{jwt}")
    public ResponseEntity<?> changedEmailVerification(@PathVariable("jwt") String jwt) {
        return ResponseEntity.ok(profileService.verification(jwt));
    }
}
