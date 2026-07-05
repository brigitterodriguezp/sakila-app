package com.app.sakila.service;

import com.app.sakila.dto.AuthRequest;
import com.app.sakila.dto.AuthResponse;
import com.app.sakila.dto.RegisterRequest;
import com.app.sakila.entity.User;
import com.app.sakila.repository.UserRepository;
import com.app.sakila.security.JwtTokenProvider;
import com.app.sakila.security.SingleSessionFilter;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SingleSessionFilter singleSessionFilter;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                       SingleSessionFilter singleSessionFilter) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.singleSessionFilter = singleSessionFilter;
    }

    @PostConstruct
    public void initDemoUsers() {
        if (!userRepository.existsByUsername("admin")) {
            var admin = new User("admin", "admin@sakila.app",
                    passwordEncoder.encode("Admin123!"), "ADMIN");
            userRepository.save(admin);
        }
        if (!userRepository.existsByUsername("user")) {
            var user = new User("user", "user@sakila.app",
                    passwordEncoder.encode("User123!"), "USER");
            userRepository.save(user);
        }
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken");
        }

        var user = new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                "USER");
        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole(), user.getId());
        singleSessionFilter.registerSession(user.getUsername(), token);

        return new AuthResponse(token, user.getUsername(), user.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole(), user.getId());
        singleSessionFilter.registerSession(user.getUsername(), token);

        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}
