package com.CardMaster.config.iam;

import com.CardMaster.security.iam.JwtFilter;
import com.CardMaster.exceptions.iam.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) //  enables @PreAuthorize annotations
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    // Constructor injection for JwtFilter and AccessDeniedHandler
    public SecurityConfig(JwtFilter jwtFilter, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtFilter = jwtFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/users/register", "/users/login").permitAll()

                        .requestMatchers("/users/logout").authenticated()
                        // Admin-only endpoints
                        .requestMatchers("/users", "/users/*", "/auditlogs/**").hasRole("ADMIN")


                        .requestMatchers("/users", "/users/*", "/auditlogs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/applications/**").hasAnyRole("CUSTOMER","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/applications/**").hasAnyRole("UNDERWRITER","ADMIN","CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/applications/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/applications/**").hasRole("ADMIN")

                        // Scores (POST + GET)
                        .requestMatchers(
                                "/applications/*/scores",
                                "/applications/*/scores/latest"
                        ).hasAnyRole("UNDERWRITER", "ADMIN")

                        // Decisions (POST + GET)
                        .requestMatchers(
                                "/applications/*/decisions",
                                "/applications/*/decisions/latest"
                        ).hasRole("UNDERWRITER")


                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler) //  custom handler
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
