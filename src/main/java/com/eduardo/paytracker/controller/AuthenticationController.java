package com.eduardo.paytracker.controller;

import com.eduardo.paytracker.dto.AuthRequestDTO;
import com.eduardo.paytracker.dto.AuthResponseDTO;
import com.eduardo.paytracker.dto.RegisterRequestDTO;
import com.eduardo.paytracker.dto.RegisterResposneDTO;
import com.eduardo.paytracker.model.User;
import com.eduardo.paytracker.service.AuthenticationService;
import com.eduardo.paytracker.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO data){
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new AuthResponseDTO(tokenJWT));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResposneDTO> register(@RequestBody @Valid RegisterRequestDTO data){
        authenticationService.registerUser(data);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResposneDTO("User successfully created!"));
    }

}
