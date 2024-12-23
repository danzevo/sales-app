package com.example.sales_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.sales_app.security.JwtFilter;

@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/admin/activate").hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/products/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/transactions/**"), new AntPathRequestMatcher("/api/reports/**"))
                .hasAnyRole("ADMIN", "KASIR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable());
        
        System.out.println("SecurityConfig: JWT Filter applied before authentication");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
