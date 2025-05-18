package com.departement.fichedevoeux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")              // Allow CORS for all paths
                        .allowedOrigins("http://localhost:3000")  // Your frontend origin (adjust port)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP methods you allow
                        .allowedHeaders("*")          // Allow all headers
                        .allowCredentials(true);      // If your frontend sends cookies or auth headers
            }
        };
    }
}

