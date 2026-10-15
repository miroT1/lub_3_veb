package com.example.student_management.config;

import com.example.student_management.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/",
                            "/html/**",
                            "/css/**",
                            "/js/**",
                            "/favicon.png",
                            "/favicon.ico"
                    ).permitAll()

                    .requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/webjars/**"
                    ).permitAll()

                    .requestMatchers("/api/auth/**").permitAll()

                    .requestMatchers(HttpMethod.GET, "/api/students/**")
                        .hasAnyRole("USER", "ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/students/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")

                    .anyRequest().authenticated()
            )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}