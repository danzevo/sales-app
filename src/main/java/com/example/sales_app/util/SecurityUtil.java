package com.example.sales_app.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
