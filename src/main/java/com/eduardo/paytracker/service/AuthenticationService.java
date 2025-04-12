package com.eduardo.paytracker.service;

import com.eduardo.paytracker.dto.RegisterRequestDTO;
import com.eduardo.paytracker.exception.ExistentUserException;
import com.eduardo.paytracker.model.User;
import com.eduardo.paytracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public void registerUser(RegisterRequestDTO data) {
        var user = userRepository.findByEmail(data.email());

        if(user != null){
            throw new ExistentUserException("This user is already registered!");
        }

        userRepository.save(new User(null, data.name(), data.email(), data.password()));
    }
}
