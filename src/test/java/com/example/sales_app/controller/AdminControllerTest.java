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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sales_app.entity.User;
import com.example.sales_app.repository.UserRepository;
import com.example.sales_app.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    void setupSecurityContext() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", null));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testActivateUserSuccess() throws Exception {
        setupSecurityContext();

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setActive(false);

        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(mockUser));

        mockMvc.perform(put("/api/admin/activate/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("User activated successfully"));

        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testActiveUserNotFound() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.empty());

        mockMvc.perform(put("/api/admin/activate/testuser"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testActiveUserError() throws Exception {
        when(userRepository.findByUsername("testuser")).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/api/admin/activate/testuser"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Database error"));
    }
}
