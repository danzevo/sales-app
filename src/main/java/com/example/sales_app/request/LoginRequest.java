package com.example.sales_app.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

    
    @Override
    public String toString() {
        return "LoginRequest{username='" + username + "', password='" + password + "'}";
    }
}
