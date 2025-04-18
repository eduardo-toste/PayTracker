package com.eduardo.paytracker.service;

import com.eduardo.paytracker.dto.RegisterRequestDTO;
import com.eduardo.paytracker.exception.ExistentUserException;
import com.eduardo.paytracker.model.User;
import com.eduardo.paytracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return user;
    }

    public void registerUser(RegisterRequestDTO data) {
        var user = userRepository.findByEmail(data.email());

        if(user != null){
            throw new ExistentUserException();
        }

        var password = passwordEncoder.encode(data.password());

        userRepository.save(new User(null, data.name(), data.email(), password));
    }
}
