package com.eduardo.paytracker.config.security;

import com.eduardo.paytracker.exception.TokenException;
import com.eduardo.paytracker.repository.UserRepository;
import com.eduardo.paytracker.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            var token = getToken(request);

            if (token != null) {
                var subject = tokenService.validateToken(token);
                var user = userRepository.findByEmail(subject);
                var authentication = new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (TokenException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            var error = new HashMap<String, Object>();
            error.put("timestamp", LocalDateTime.now().toString());
            error.put("status", HttpStatus.UNAUTHORIZED.value());
            error.put("error", "Unauthorized");
            error.put("message", ex.getMessage());

            var writer = response.getWriter();
            writer.write(new ObjectMapper().writeValueAsString(error));
            writer.flush();
        }
    }

    private String getToken(HttpServletRequest request){
        var authoritzationHeader = request.getHeader("Authorization");

        if(authoritzationHeader != null){
            return authoritzationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
