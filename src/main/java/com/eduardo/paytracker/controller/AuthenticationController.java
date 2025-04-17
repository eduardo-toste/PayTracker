package com.eduardo.paytracker.controller;

import com.eduardo.paytracker.dto.AuthRequestDTO;
import com.eduardo.paytracker.dto.AuthResponseDTO;
import com.eduardo.paytracker.dto.RegisterRequestDTO;
import com.eduardo.paytracker.dto.RegisterResposneDTO;
import com.eduardo.paytracker.model.User;
import com.eduardo.paytracker.service.AuthenticationService;
import com.eduardo.paytracker.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login e cadastro de usuários")
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Autentica o usuário e retorna um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou formato incorreto"),
            @ApiResponse(responseCode = "401", description = "Usuário ou senha incorretos"),
            @ApiResponse(responseCode = "500", description = "Erro interno durante o login")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new AuthResponseDTO(tokenJWT));
    }

    @Operation(summary = "Registra um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao registrar usuário")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResposneDTO> register(@RequestBody @Valid RegisterRequestDTO data) {
        authenticationService.registerUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResposneDTO("User successfully created!"));
    }

}