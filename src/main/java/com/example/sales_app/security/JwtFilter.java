package com.example.sales_app.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.sales_app.entity.User;
import com.example.sales_app.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        try {
            String authHeader = request.getHeader("Authorization");
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                if (token.isEmpty()) {
                    System.out.println("Token is empty!");
                } else {
                    Claims claims = jwtUtil.extractClaims(token);

                    if(claims != null) {
                        String username = claims.getSubject();
                        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

                        if(user.isActive()) {
                            UserDetails userDetails = org.springframework.security.core.userdetails.User
                                                    .withUsername(user.getUsername())
                                                    .password(user.getPassword())
                                                    .roles(user.getRole().name())
                                                    .build();

                            SecurityContextHolder.getContext().setAuthentication(
                                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                                )
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
        }
        // Ensure the filter chain continues
        filterChain.doFilter(request, response);
    }
}
