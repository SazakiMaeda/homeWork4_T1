package com.sazakimaeda.hm4_autorization.controller;

import com.sazakimaeda.hm4_autorization.dto.JWTAuthenticationDto;
import com.sazakimaeda.hm4_autorization.dto.RefreshTokenDto;
import com.sazakimaeda.hm4_autorization.dto.UserCredentialsDto;
import com.sazakimaeda.hm4_autorization.model.User;
import com.sazakimaeda.hm4_autorization.repository.UserRepository;
import com.sazakimaeda.hm4_autorization.request.RegistrationRequest;
import com.sazakimaeda.hm4_autorization.service.AuthService;
import com.sazakimaeda.hm4_autorization.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<JWTAuthenticationDto> register(@RequestBody RegistrationRequest dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthenticationDto> login(@RequestBody UserCredentialsDto credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }

    @PostMapping("/token/refresh")
    @PreAuthorize("permitAll()")
    public ResponseEntity<JWTAuthenticationDto> refresh(@RequestBody RefreshTokenDto dto) {
        return ResponseEntity.ok(authService.refreshToken(dto));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenDto dto) {
        authService.logout(dto);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/upgrade")
    public ResponseEntity<User> upgrade(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userServiceImpl.upgradeToPremium(email));
    }

    @GetMapping("/premium")
    public String premiumDashboard() {
        return "You have premium account.";
    }

    @GetMapping("/dashboard")
    public String getAdminDashboard() {
        return userRepository.findAll().toString();
    }
}
