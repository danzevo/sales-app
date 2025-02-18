package com.example.sales_app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sales_app.request.ReportRequest;
import com.example.sales_app.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ReportService reportService;

    @InjectMocks 
    private ReportController reportController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testGenerateReportSuccess() throws Exception {
        ReportRequest request = new ReportRequest();
        request.setStartDate(LocalDateTime.of(2024, 2, 1, 0, 0));
        request.setEndDate(LocalDateTime.of(2024, 2, 10, 23, 59));

        List<String> expectedResponse = Arrays.asList("Report 1", "Report 2");

        when(reportService.generateReport(any(ReportRequest.class))).thenReturn((List) expectedResponse);

        mockMvc.perform(post("/api/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void testGenerateReportFailure() throws Exception {
        ReportRequest request = new ReportRequest();        
        request.setStartDate(LocalDateTime.of(2024, 2, 1, 0, 0));
        request.setEndDate(LocalDateTime.of(2024, 2, 10, 23, 59));

        when(reportService.generateReport(any(ReportRequest.class))).thenThrow(new RuntimeException("Report generation failed"));

        mockMvc.perform(post("/api/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Report generation failed"));
    }
}
