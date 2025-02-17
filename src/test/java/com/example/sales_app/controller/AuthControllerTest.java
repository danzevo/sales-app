package com.example.sales_app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sales_app.request.LoginRequest;
import com.example.sales_app.request.RegisterRequest;
import com.example.sales_app.response.UserResponse;
import com.example.sales_app.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        when(authService.login(any(LoginRequest.class))).thenReturn("mocked-jwt-token");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-jwt-token"));
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        when(authService.login(any(LoginRequest.class))).thenThrow(new IllegalArgumentException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("user@example.com", "password");
        doNothing().when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test 
    void testRegisterFailure() throws Exception {
        RegisterRequest request = new RegisterRequest("user@example.com", "password");
        doThrow(new IllegalArgumentException("Email already exists"))
            .when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    @Test
    void testGetCurrentUserSuccess() throws Exception {
        UserResponse userResponse = new UserResponse("admin", "ADMIN", true);
        when(authService.getCurrentUser()).thenReturn(userResponse);

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.active").value("true"));
    }
}
