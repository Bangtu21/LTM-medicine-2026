package com.example.medicinemanagement.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TcpConfig {
    public String getHost() {
        return "localhost";
    }

    public int getPort() {
        return 9999;
    }
}
