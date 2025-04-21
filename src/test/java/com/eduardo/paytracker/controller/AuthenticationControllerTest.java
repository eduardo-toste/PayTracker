package com.eduardo.paytracker.controller;

import com.eduardo.paytracker.dto.AuthRequestDTO;
import com.eduardo.paytracker.dto.RegisterRequestDTO;
import com.eduardo.paytracker.exception.ExistentUserException;
import com.eduardo.paytracker.model.User;
import com.eduardo.paytracker.repository.UserRepository;
import com.eduardo.paytracker.service.AuthenticationService;
import com.eduardo.paytracker.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(AuthenticationController.class)
@Import(AuthenticationControllerTest.MockBeans.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @TestConfiguration
    static class MockBeans {
        @Bean
        AuthenticationManager authenticationManager() {
            return mock(AuthenticationManager.class);
        }

        @Bean
        TokenService tokenService() {
            return mock(TokenService.class);
        }

        @Bean
        AuthenticationService authenticationService() {
            return mock(AuthenticationService.class);
        }

        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }
    }

    @jakarta.annotation.Resource
    private MockMvc mockMvc;

    @jakarta.annotation.Resource
    private AuthenticationManager manager;

    @jakarta.annotation.Resource
    private TokenService tokenService;

    @jakarta.annotation.Resource
    private AuthenticationService authenticationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenValidCredentials_whenLogin_thenReturnsToken() throws Exception {
        AuthRequestDTO dto = new AuthRequestDTO("edu@email.com", "senha123");
        User mockUser = new User(1L, "Edu", "edu@email.com", "encodedSenha");

        var authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(manager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateToken(mockUser)).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("mock-jwt-token")));
    }

    @Test
    void givenInvalidCredentials_whenLogin_thenReturnsUnauthorized() throws Exception {
        AuthRequestDTO dto = new AuthRequestDTO("wrong@email.com", "wrong");

        when(manager.authenticate(any())).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenValidData_whenRegister_thenReturnsCreated() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("Edu2", "edu2@email.com", "senha123");

        // Garante que não lançará exceção
        doNothing().when(authenticationService).registerUser(any());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("User successfully created!")));

        verify(authenticationService).registerUser(any());
    }

    @Test
    void givenExistingUser_whenRegister_thenReturnsConflict() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("Edu", "edu@email.com", "senha123");

        doThrow(new ExistentUserException()).when(authenticationService).registerUser(any());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }
}
