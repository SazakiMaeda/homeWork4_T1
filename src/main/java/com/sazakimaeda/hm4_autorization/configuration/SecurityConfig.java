package com.sazakimaeda.hm4_autorization.configuration;

import com.sazakimaeda.hm4_autorization.filter.JWTFilter;
import com.sazakimaeda.hm4_autorization.service.JWTService;
import com.sazakimaeda.hm4_autorization.service.UserService;
import com.sazakimaeda.hm4_autorization.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/register", "/api/v1/login",
                                "/api/v1/token/refresh").permitAll()
                        .requestMatchers("/api/v1/dashboard").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/upgrade").hasAnyAuthority("GUEST", "PREMIUM_USER")
                        .requestMatchers("/api/v1/premium").hasAuthority("PREMIUM_USER")
                        .requestMatchers("/api/v1/logout").hasAnyAuthority(
                                "GUEST", "PREMIUM_USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
