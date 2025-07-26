package com.sazakimaeda.hm4_autorization.service;

import com.sazakimaeda.hm4_autorization.dto.JWTAuthenticationDto;
import com.sazakimaeda.hm4_autorization.dto.RefreshTokenDto;
import com.sazakimaeda.hm4_autorization.dto.UserCredentialsDto;
import com.sazakimaeda.hm4_autorization.entity.Role;
import com.sazakimaeda.hm4_autorization.model.TokenWhitelist;
import com.sazakimaeda.hm4_autorization.model.User;
import com.sazakimaeda.hm4_autorization.repository.UserRepository;
import com.sazakimaeda.hm4_autorization.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final TokenWhitelist tokenWhitelist;

    public JWTAuthenticationDto register(RegistrationRequest dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new RuntimeException("User already exists");
        });

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(Role.GUEST);

        userRepository.save(user);

        JWTAuthenticationDto tokens = jwtService.generateAuthenticationToken(user.getEmail());
        tokenWhitelist.addToken(tokens.getRefreshToken());

        return tokens;
    }

    public JWTAuthenticationDto login(UserCredentialsDto credentials) {
        User user = userRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        JWTAuthenticationDto tokens = jwtService.generateAuthenticationToken(user.getEmail());
        tokenWhitelist.addToken(tokens.getRefreshToken());

        return tokens;
    }

    public JWTAuthenticationDto refreshToken(RefreshTokenDto dto) {
        if (!jwtService.validateJWTToken(dto.getRefreshToken()) ||
            !tokenWhitelist.contains(dto.getRefreshToken())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String email = jwtService.getEmailFromToken(dto.getRefreshToken());
        JWTAuthenticationDto tokens = jwtService.refreshBaseToken(email, dto.getRefreshToken());

        return tokens;
    }


    public void logout(RefreshTokenDto dto) {
        tokenWhitelist.removeToken(dto.getRefreshToken());
    }

}
