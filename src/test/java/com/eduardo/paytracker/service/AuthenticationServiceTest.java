package com.eduardo.paytracker.service;

import com.eduardo.paytracker.dto.RegisterRequestDTO;
import com.eduardo.paytracker.exception.ExistentUserException;
import com.eduardo.paytracker.model.User;
import com.eduardo.paytracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void registerUser_WhenUserDoesNotExist_ShouldSaveUser() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Eduardo", "eduardo@email.com", "senha123");

        when(userRepository.findByEmail(dto.email())).thenReturn(null);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedSenha123");

        authenticationService.registerUser(dto);

        verify(userRepository).save(argThat(user ->
                user.getName().equals("Eduardo") &&
                        user.getEmail().equals("eduardo@email.com") &&
                        user.getPassword().equals("encodedSenha123")
        ));
    }

    @Test
    void registerUser_WhenUserAlreadyExists_ShouldThrowException() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Eduardo", "eduardo@email.com", "senha123");

        when(userRepository.findByEmail(dto.email())).thenReturn(new User());
        assertThrows(ExistentUserException.class, () -> authenticationService.registerUser(dto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void loadUserByUsername_WhenExists_ShouldReturnUser() {
        User user = new User(1L, "Eduardo", "eduardo@email.com", "senha123");
        when(userRepository.findByEmail("eduardo@email.com")).thenReturn(user);

        UserDetails result = authenticationService.loadUserByUsername("eduardo@email.com");

        assertEquals("eduardo@email.com", result.getUsername());
    }

    @Test
    void loadUserByUsername_WhenNotExists_ShouldThrowException() {
        when(userRepository.findByEmail("naoexiste@email.com")).thenReturn(null);

        assertThrows(InternalAuthenticationServiceException.class, () -> {
            authenticationService.loadUserByUsername("naoexiste@email.com");
        });
    }

}